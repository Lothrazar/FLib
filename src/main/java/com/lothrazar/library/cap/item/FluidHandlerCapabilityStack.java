package com.lothrazar.library.cap.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * IFluidHandlerItem that reads/writes a single fluid to an ItemStack's CustomData component.
 * In NeoForge 1.21+, register this via RegisterCapabilitiesEvent:
 *   event.registerItem(Capabilities.FluidHandler.ITEM,
 *       (stack, ctx) -> new FluidHandlerCapabilityStack(stack, capacity), myItem);
 */
public class FluidHandlerCapabilityStack implements IFluidHandlerItem {

  public static final String FLUID_NBT_KEY = "Fluid";
  protected ItemStack container;
  protected int capacity;

  public FluidHandlerCapabilityStack(ItemStack container, int capacity) {
    this.container = container;
    this.capacity = capacity;
  }

  @Override
  public ItemStack getContainer() {
    return container;
  }

  private CompoundTag getContainerTag() {
    return container.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
  }

  private void setContainerTag(CompoundTag tag) {
    container.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public FluidStack getFluid() {
    CompoundTag tag = getContainerTag();
    if (!tag.contains(FLUID_NBT_KEY)) {
      return FluidStack.EMPTY;
    }
    CompoundTag fluidTag = tag.getCompound(FLUID_NBT_KEY);
    ResourceLocation fluidId = ResourceLocation.tryParse(fluidTag.getString("id"));
    int amount = fluidTag.getInt("amount");
    if (fluidId == null) return FluidStack.EMPTY;
    Fluid fluid = BuiltInRegistries.FLUID.getOptional(fluidId).orElse(null);
    if (fluid == null || fluid == Fluids.EMPTY) return FluidStack.EMPTY;
    return new FluidStack(fluid, amount);
  }

  public void setFluid(FluidStack fluid) {
    CompoundTag tag = getContainerTag();
    if (fluid.isEmpty()) {
      tag.remove(FLUID_NBT_KEY);
    } else {
      CompoundTag fluidTag = new CompoundTag();
      fluidTag.putString("id", BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
      fluidTag.putInt("amount", fluid.getAmount());
      tag.put(FLUID_NBT_KEY, fluidTag);
    }
    setContainerTag(tag);
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return getFluid();
  }

  @Override
  public int getTankCapacity(int tank) {
    return capacity;
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return true;
  }

  @Override
  public int fill(FluidStack resource, FluidAction doFill) {
    if (container.getCount() != 1 || resource.isEmpty() || !canFillFluidType(resource)) {
      return 0;
    }
    FluidStack contained = getFluid();
    if (contained.isEmpty()) {
      int fillAmount = Math.min(capacity, resource.getAmount());
      if (doFill.execute()) {
        FluidStack filled = resource.copy();
        filled.setAmount(fillAmount);
        setFluid(filled);
      }
      return fillAmount;
    } else {
      if (FluidStack.isSameFluidSameComponents(contained, resource)) {
        int fillAmount = Math.min(capacity - contained.getAmount(), resource.getAmount());
        if (doFill.execute() && fillAmount > 0) {
          contained.grow(fillAmount);
          setFluid(contained);
        }
        return fillAmount;
      }
      return 0;
    }
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    if (container.getCount() != 1 || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, getFluid())) {
      return FluidStack.EMPTY;
    }
    return drain(resource.getAmount(), action);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    if (container.getCount() != 1 || maxDrain <= 0) {
      return FluidStack.EMPTY;
    }
    FluidStack contained = getFluid();
    if (contained.isEmpty() || !canDrainFluidType(contained)) {
      return FluidStack.EMPTY;
    }
    final int drainAmount = Math.min(contained.getAmount(), maxDrain);
    FluidStack drained = contained.copy();
    drained.setAmount(drainAmount);
    if (action.execute()) {
      contained.shrink(drainAmount);
      if (contained.isEmpty()) {
        setContainerToEmpty();
      } else {
        setFluid(contained);
      }
    }
    return drained;
  }

  public boolean canFillFluidType(FluidStack fluid) {
    return true;
  }

  public boolean canDrainFluidType(FluidStack fluid) {
    return true;
  }

  protected void setContainerToEmpty() {
    CompoundTag tag = getContainerTag();
    tag.remove(FLUID_NBT_KEY);
    setContainerTag(tag);
  }

  @Override
  public String toString() {
    return "FluidHandlerCapabilityStack [container=" + container + ", capacity=" + capacity + "]";
  }
}
