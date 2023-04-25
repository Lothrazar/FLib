package com.lothrazar.library.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerUtil {

  public static void swapArmorStand(ArmorStand stand, Player player, InteractionHand hand) {
    ItemStack heldPlayer = player.getItemInHand(hand).copy();
    ItemStack heldStand = stand.getItemInHand(hand).copy();
    EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    stand.setItemSlot(slot, heldPlayer);
    player.setItemSlot(slot, heldStand);
  }

  public static boolean isTamedByPlayer(AbstractHorse horse, Player dmgOwner) {
    return horse.isTamed() && horse.getOwnerUUID() != null &&
        horse.getOwnerUUID().equals(dmgOwner.getUUID());
  }

  public static boolean isTamedByPlayer(TamableAnimal horse, Player dmgOwner) {
    return horse.isTame() && horse.getOwnerUUID() != null &&
        horse.getOwnerUUID().equals(dmgOwner.getUUID());
  }

  public static void clearAllExp(Player player) {
    player.experienceProgress = 0;
    player.experienceLevel = 0;
    player.totalExperience = 0;
  }
}
