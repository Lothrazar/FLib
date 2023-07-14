package com.lothrazar.library.cap.item;

import com.lothrazar.library.cap.CustomEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class CapabilityProviderEnergyStack implements ICapabilitySerializable<CompoundTag> {

  private static final String NBTENERGY = "energy";
  CustomEnergyStorage energy;
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);

  public CapabilityProviderEnergyStack(int max) {
    energy = new CustomEnergyStorage(max, max);
    energyCap = LazyOptional.of(() -> energy);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.put(NBTENERGY, energy.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    energy.deserializeNBT(nbt.getCompound(NBTENERGY));
  }

  @Override
  public String toString() {
    return "CapabilityProviderEnergyStack [energy=" + energy + "]";
  }
}
