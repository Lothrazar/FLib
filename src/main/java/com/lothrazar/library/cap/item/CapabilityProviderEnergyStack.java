package com.lothrazar.library.cap.item;

import com.lothrazar.library.cap.CustomEnergyStorage;
import net.minecraft.nbt.CompoundTag;

/**
 * Simple energy storage wrapper for items.
 * In NeoForge 1.21+, attach this to an item via RegisterCapabilitiesEvent:
 *   event.registerItem(Capabilities.EnergyStorage.ITEM,
 *       (stack, ctx) -> new CapabilityProviderEnergyStack(maxEnergy).getEnergyStorage(), myItem);
 */
public class CapabilityProviderEnergyStack {

  private static final String NBTENERGY = "energy";
  private final CustomEnergyStorage energy;

  public CapabilityProviderEnergyStack(int max) {
    energy = new CustomEnergyStorage(max, max);
  }

  public CustomEnergyStorage getEnergyStorage() {
    return energy;
  }

  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.put(NBTENERGY, energy.serializeNBT());
    return tag;
  }

  public void deserializeNBT(CompoundTag nbt) {
    energy.deserializeNBT(nbt.getCompound(NBTENERGY));
  }

  @Override
  public String toString() {
    return "CapabilityProviderEnergyStack [energy=" + energy + "]";
  }
}
