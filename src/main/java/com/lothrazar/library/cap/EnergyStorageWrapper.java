package com.lothrazar.library.cap;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.energy.EnergyStorage;

/**
 * For use with items, tile entities, etc.
 *
 * @see cyclic
 */
public class EnergyStorageWrapper extends EnergyStorage {

  public static final String NBTENERGY = "energy";

  public EnergyStorageWrapper(int capacity, int maxTransfer) {
    super(capacity, maxTransfer);
  }

  public void setEnergy(int energyIn) {
    if (energyIn < 0) {
      energyIn = 0;
    }
    if (energyIn > getMaxEnergyStored()) {
      energyIn = getMaxEnergyStored();
    }
    this.energy = energyIn;
  }

  /**
   * Serialize energy to a CompoundTag (e.g. for block entity NBT).
   * Use this instead of serializeNBT() when embedding energy inside a larger compound.
   */
  public CompoundTag saveToTag() {
    CompoundTag tag = new CompoundTag();
    tag.putInt(NBTENERGY, getEnergyStored());
    return tag;
  }

  /**
   * Deserialize energy from a CompoundTag (e.g. from block entity NBT).
   */
  public void loadFromTag(CompoundTag tag) {
    setEnergy(tag.getInt(NBTENERGY));
  }

  // Override INBTSerializable<IntTag> from EnergyStorage with provider signatures
  @Override
  public IntTag serializeNBT(HolderLookup.Provider provider) {
    return IntTag.valueOf(getEnergyStored());
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
    if (nbt instanceof IntTag intNbt) {
      setEnergy(intNbt.getAsInt());
    }
    //legacy fallback: vanilla EnergyStorage used to serialize as a CompoundTag with an
    //"energy" key. Worlds saved before the IntTag switch still arrive in that shape.
    else if (nbt instanceof CompoundTag compoundNbt) {
      setEnergy(compoundNbt.getInt(NBTENERGY));
    }
    else if (nbt != null) {
      throw new IllegalArgumentException("Can not deserialize EnergyStorageWrapper from " + nbt.getClass().getSimpleName());
    }
  }
}
