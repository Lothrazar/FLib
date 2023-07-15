package com.lothrazar.library.cap.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

  public static Capability<PlayerCapabilityStorage> PLAYERCAP = CapabilityManager.get(new CapabilityToken<>() {
    //empty by design
  });
  private PlayerCapabilityStorage playerMana = null;
  private final LazyOptional<PlayerCapabilityStorage> opt = LazyOptional.of(this::createMe);

  private PlayerCapabilityStorage createMe() {
    if (playerMana == null) {
      playerMana = new PlayerCapabilityStorage();
    }
    return playerMana;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap) {
    if (cap == PLAYERCAP) {
      return opt.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return getCapability(cap);
  }

  @Override
  public CompoundTag serializeNBT() {
    return createMe().write();
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    createMe().read(nbt);
  }
}
