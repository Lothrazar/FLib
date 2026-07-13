package com.lothrazar.library.cap.item;

import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.nbt.CompoundTag;

/**
 * Simple energy storage wrapper for items.
 * TODO 26.1 port: the capability key moved to Capabilities.Energy.ITEM, which now expects a
 * transactional EnergyHandler rather than IEnergyStorage - registration needs a fresh look
 * (see net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler for the modern equivalent).
 * In NeoForge 1.21.x, this was attached to an item via RegisterCapabilitiesEvent:
 *   event.registerItem(Capabilities.EnergyStorage.ITEM,
 *       (stack, ctx) -> new CapabilityProviderEnergyStack(maxEnergy).getEnergyStorage(), myItem);
 */
public class CapabilityProviderEnergyStack {

  private static final String NBTENERGY = "energy";
  private final EnergyStorageWrapper energy;

  public CapabilityProviderEnergyStack(int max) {
    energy = new EnergyStorageWrapper(max, max);
  }

  public EnergyStorageWrapper getEnergyStorage() {
    return energy;
  }

  public CompoundTag serializeNBT() {
    return energy.saveToTag();
  }

  public void deserializeNBT(CompoundTag nbt) {
    energy.loadFromTag(nbt);
  }

  @Override
  public String toString() {
    return "CapabilityProviderEnergyStack [energy=" + energy + "]";
  }
}
