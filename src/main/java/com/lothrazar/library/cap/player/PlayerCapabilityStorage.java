package com.lothrazar.library.cap.player;

import net.minecraft.nbt.CompoundTag;

public class PlayerCapabilityStorage {

  int mana;

  public PlayerCapabilityStorage(int readInt) {
    mana = readInt;
  }

  public PlayerCapabilityStorage() {}

  public PlayerCapabilityStorage(CompoundTag tag) {
    this.read(tag);
  }

  public int getMana() {
    return mana;
  }

  public void setMana(int mana) {
    this.mana = mana;
  }

  public void addMana(int mana) {
    this.mana += mana;
  }

  public void copyFrom(PlayerCapabilityStorage source) {
    mana = source.mana;
  }

  public CompoundTag write() {
    CompoundTag compound = new CompoundTag();
    compound.putInt("mana", mana);
    return compound;
  }

  public void read(CompoundTag compound) {
    mana = compound.getInt("mana");
  }

  @Override
  public String toString() {
    return "PlayerCapabilityStorage [mana=" + mana + "]";
  }
}