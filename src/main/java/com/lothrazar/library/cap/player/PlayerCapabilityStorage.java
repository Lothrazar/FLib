package com.lothrazar.library.cap.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public class PlayerCapabilityStorage {

  public static final Codec<PlayerCapabilityStorage> CODEC = RecordCodecBuilder.create(instance ->
      instance.group(Codec.INT.fieldOf("mana").forGetter(PlayerCapabilityStorage::getMana))
          .apply(instance, mana -> new PlayerCapabilityStorage(mana)));

  int mana;

  public PlayerCapabilityStorage(int mana) {
    this.mana = mana;
  }

  public PlayerCapabilityStorage() {}

  @Deprecated // i think
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
