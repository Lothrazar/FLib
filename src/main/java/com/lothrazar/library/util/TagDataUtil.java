package com.lothrazar.library.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TagDataUtil {

  public static final String SKULLOWNER = "SkullOwner";

  public static ItemStack buildNamedPlayerSkull(Player player) {
    return buildNamedPlayerSkull(player.getDisplayName().getString());
  }

  public static ItemStack buildNamedPlayerSkull(String displayNameString) {
    ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
    skull.set(net.minecraft.core.component.DataComponents.PROFILE, new net.minecraft.world.item.component.ResolvableProfile(new com.mojang.authlib.GameProfile(net.minecraft.Util.NIL_UUID, displayNameString)));
    return skull;
  }

  public static ItemStack buildSkullFromTag(CompoundTag player) {
    if (player.contains(SKULLOWNER)) {
      return buildNamedPlayerSkull(player.getString(SKULLOWNER));
    }
    return new ItemStack(Items.PLAYER_HEAD);
  }

  public static void setItemStackBlockPos(ItemStack item, BlockPos pos) {
    if (pos == null || item.isEmpty()) {
      return;
    }
    setItemStackNBTVal(item, "xpos", pos.getX());
    setItemStackNBTVal(item, "ypos", pos.getY());
    setItemStackNBTVal(item, "zpos", pos.getZ());
  }

  public static void putBlockPos(CompoundTag tag, BlockPos pos) {
    tag.putInt("xpos", pos.getX());
    tag.putInt("ypos", pos.getY());
    tag.putInt("zpos", pos.getZ());
  }

  public static BlockPos getItemStackBlockPos(ItemStack item) {
    CompoundTag tag = getItemStackNBT(item);
    if (!tag.contains("xpos")) {
      return null;
    }
    return getBlockPos(tag);
  }

  public static BlockPos getBlockPos(CompoundTag tag) {
    return new BlockPos(tag.getInt("xpos"), tag.getInt("ypos"), tag.getInt("zpos"));
  }

  public static void setItemStackNBTVal(ItemStack item, String prop, int value) {
    if (item.isEmpty()) {
      return;
    }
    CompoundTag tag = getItemStackNBT(item);
    tag.putInt(prop, value);
    item.set(com.lothrazar.library.registry.FlibDataComponents.CUSTOM_NBT_BUCKET.get(), tag);
  }

  public static CompoundTag getItemStackNBT(ItemStack held) {
    return held.getOrDefault(com.lothrazar.library.registry.FlibDataComponents.CUSTOM_NBT_BUCKET.get(), new CompoundTag());
  }
}
