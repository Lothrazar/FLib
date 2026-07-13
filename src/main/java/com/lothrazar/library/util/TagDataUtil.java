package com.lothrazar.library.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ResolvableProfile;

public class TagDataUtil {

  public static final String SKULLOWNER = "SkullOwner";

  public static ItemStack buildNamedPlayerSkull(Player player) {
    return buildNamedPlayerSkull(player.getDisplayName().getString());
  }

  public static ItemStack buildNamedPlayerSkull(String displayNameString) {

    ItemStack skull = new ItemStack(Items.PLAYER_HEAD);

    skull.set(DataComponents.PROFILE, ResolvableProfile.createResolved(new GameProfile(Util.NIL_UUID, displayNameString)));
    return skull;
  }

  public static void setItemStackBlockPos(ItemStack item, BlockPos pos) {
    if (pos == null || item.isEmpty()) {
      return;
    }
    TagDataUtil.setItemStackNBTVal(item, "xpos", pos.getX());
    TagDataUtil.setItemStackNBTVal(item, "ypos", pos.getY());
    TagDataUtil.setItemStackNBTVal(item, "zpos", pos.getZ());
  }

  public static void putBlockPos(CompoundTag tag, BlockPos pos) {
    tag.putInt("xpos", pos.getX());
    tag.putInt("ypos", pos.getY());
    tag.putInt("zpos", pos.getZ());
  }

  public static BlockPos getItemStackBlockPos(ItemStack item) {
    if (item.isEmpty()) {
      return null;
    }
    CustomData data = item.get(DataComponents.CUSTOM_DATA);
    if (data == null) {
      return null;
    }
    CompoundTag tag = data.copyTag();
    if (!tag.contains("xpos")) {
      return null;
    }
    return getBlockPos(tag);
  }

  public static BlockPos getBlockPos(CompoundTag tag) {
    return new BlockPos(tag.getIntOr("xpos", 0), tag.getIntOr("ypos", 0), tag.getIntOr("zpos", 0));
  }

  public static void setItemStackNBTVal(ItemStack item, String prop, int value) {
    if (item.isEmpty()) {
      return;
    }
    CompoundTag tag = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.putInt(prop, value);
    item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public static CompoundTag getItemStackNBT(ItemStack held) {
    return held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
  }
}
