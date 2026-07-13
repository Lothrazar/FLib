package com.lothrazar.library.cap.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * IEnergyStorage that reads/writes energy to an ItemStack's CustomData component.
 * TODO 26.1 port: the capability key moved to Capabilities.Energy.ITEM, which now expects a
 * transactional EnergyHandler rather than IEnergyStorage - registration needs a fresh look.
 * In NeoForge 1.21.x, this was registered via RegisterCapabilitiesEvent:
 *   event.registerItem(Capabilities.EnergyStorage.ITEM,
 *       (stack, ctx) -> new EnergyCapabilityItemStack(stack, maxEnergy), myItem);
 */
public class EnergyCapabilityItemStack implements IEnergyStorage {

  public static final String NBTENERGY = "energy";
  private final ItemStack stack;
  private final int max;

  public EnergyCapabilityItemStack(final ItemStack stack, int capacity) {
    this.stack = stack;
    this.max = capacity;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    if (!canReceive()) return 0;
    int stored = getEnergyStored();
    int received = Math.min(max - stored, Math.min(max / 4, maxReceive));
    if (!simulate) setEnergyStored(stored + received);
    return received;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    if (!canExtract()) return 0;
    int stored = getEnergyStored();
    int extracted = Math.min(stored, maxExtract);
    if (!simulate) setEnergyStored(stored - extracted);
    return extracted;
  }

  @Override
  public int getEnergyStored() {
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    return data != null ? data.copyTag().getIntOr(NBTENERGY, 0) : 0;
  }

  @Override
  public int getMaxEnergyStored() {
    return max;
  }

  @Override
  public boolean canExtract() {
    return true;
  }

  @Override
  public boolean canReceive() {
    return true;
  }

  private void setEnergyStored(int value) {
    CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.putInt(NBTENERGY, Math.max(0, Math.min(value, max)));
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  @Override
  public String toString() {
    return "EnergyCapabilityItemStack [energy=" + getEnergyStored() + ", max=" + max + "]";
  }
}
