package com.lothrazar.library.cap;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.stream.Stream;

/**
 * Wraps two {@link ItemStackHandler}s: Input and Output. Input's slots come first then the Output's slots come after. Items can only be inserted into Input. Items can only be extracted from Output.
 * Note that the above only applies to operations on the wrapper, the backing handlers are not restricted. For persistence, either the backing {@link ItemStackHandler}s can be saved, or the wrapper
 * itself.
 *
 * @see com/lothrazar/cyclic/capabilities/
 */
public class ItemStackHandlerWrapper implements IItemHandler, IItemHandlerModifiable, ValueIOSerializable {

  public static final String NBT_INPUT = "Input";
  public static final String NBT_OUTPUT = "Output";
  protected final ItemStackHandler input;
  protected final ItemStackHandler output;
  private int[] slotNumbers;

  public ItemStackHandlerWrapper(ItemStackHandler input, ItemStackHandler output) {
    this.input = input;
    this.output = output;
    int size = input.getSlots() + output.getSlots();
    //for hopper & WorldlyContainer support
    this.slotNumbers = Stream.iterate(0, n -> n + 1).limit(size)
        .mapToInt(i -> i).toArray();
  }

  /**
   * Calls with the correct handler, slot for the handler and if it matches the input handler.
   */
  protected <T> T withHandler(int externalSlot, HandlerCallback<T> callback) {
    int numInputSlots = input.getSlots();
    boolean isInput = externalSlot < numInputSlots;
    int internalSlot = isInput ? externalSlot : externalSlot - numInputSlots;
    ItemStackHandler handler = isInput ? input : output;
    return callback.apply(handler, internalSlot, isInput);
  }

  /**
   * For functions that return void.
   *
   * @see ItemStackHandlerWrapper#withHandler(int, HandlerCallback)
   */
  protected void withHandlerV(int slot, HandlerCallbackVoid func) {
    withHandler(slot, (h, s, isInput) -> {
      func.apply(h, s, isInput);
      return false; // Because generics can't be void >.<
    });
  }

  @Override
  public int getSlots() {
    return input.getSlots() + output.getSlots();
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return withHandler(slot, (h, s, isInput) -> h.getStackInSlot(s));
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    return withHandler(slot, (h, s, isInput) -> isInput ? h.insertItem(s, stack, simulate) : stack);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return withHandler(slot, (h, s, isInput) -> isInput ? ItemStack.EMPTY : h.extractItem(s, amount, simulate));
  }

  @Override
  public int getSlotLimit(int slot) {
    return withHandler(slot, (h, s, isInput) -> h.getSlotLimit(s));
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return withHandler(slot, (h, s, isInput) -> isInput && h.isItemValid(s, stack));
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    withHandlerV(slot, (h, s, isInput) -> h.setStackInSlot(s, stack));
  }

  @Override
  public void serialize(ValueOutput output0) {
    input.serialize(output0.child(NBT_INPUT));
    output.serialize(output0.child(NBT_OUTPUT));
  }

  @Override
  public void deserialize(ValueInput input0) {
    input.deserialize(input0.childOrEmpty(NBT_INPUT));
    output.deserialize(input0.childOrEmpty(NBT_OUTPUT));
  }

  @FunctionalInterface
  protected interface HandlerCallback<T> {

    T apply(ItemStackHandler handler, int slot, boolean isInput);
  }

  @FunctionalInterface
  protected interface HandlerCallbackVoid {

    void apply(ItemStackHandler handler, int slot, boolean isInput);
  }

  /**
   * Override this to limit face and direction specific restrictions. Default is all faces for every direction
   * 
   * For use with WorldlyContainer.java
   */
  public int[] getSlotsForFace(Direction direction) {
    return slotNumbers;
  }

  /**
   * Support for non-capability interfaces like hoppers to support in-only and out-only. For use with WorldlyContainer.java
   */
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return i < this.input.getSlots();
  }

  /**
   * Support for non-capability interfaces like hoppers to support in-only and out-only. For use with WorldlyContainer.java
   */
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return i >= this.input.getSlots();
  }
}
