package com.lothrazar.library.util;

import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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

  public static double getExpTotal(Player player) {
    //  validateExpPositive(player);
    int level = player.experienceLevel;
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    double totalExp = getXpForLevel(level);
    double progress = Math.round(player.getXpNeededForNextLevel() * player.experienceProgress);
    totalExp += (int) progress;
    return totalExp;
  }

  public static boolean isPlayerCrouching(Entity entity) {
    return entity instanceof Player && ((Player) entity).isCrouching();
  }

  public static int getXpForLevel(int level) {
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    int totalExp = 0;
    if (level <= 15) {
      totalExp = level * level + 6 * level;
    }
    else if (level <= 30) {
      totalExp = (int) (2.5 * level * level - 40.5 * level + 360);
    }
    else {
      // level >= 31
      totalExp = (int) (4.5 * level * level - 162.5 * level + 2220);
    }
    return totalExp;
  }

  public static ItemStack getPlayerItemIfHeld(Player player) {
    ItemStack wand = player.getMainHandItem();
    if (wand.isEmpty()) {
      wand = player.getOffhandItem();
    }
    return wand;
  }

  public static int getFirstSlotWithBlock(Player player, BlockState targetState) {
    int ret = -1;
    ItemStack stack;
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      stack = player.getInventory().getItem(i);
      if (!stack.isEmpty() &&
          stack.getItem() != null &&
          Block.byItem(stack.getItem()) == targetState.getBlock()) {
        return i;
      }
    }
    return ret;
  }

  public static BlockState getBlockstateFromSlot(Player player, int slot) {
    ItemStack stack = player.getInventory().getItem(slot);
    if (!stack.isEmpty() &&
        stack.getItem() != null &&
        Block.byItem(stack.getItem()) != null) {
      Block b = Block.byItem(stack.getItem());
      return b.defaultBlockState();
    }
    return null;
  }

  public static void decrStackSize(Player player, int slot) {
    if (player.isCreative() == false && slot >= 0) {
      player.getInventory().removeItem(slot, 1);
    }
  }

  public static Item getItemArmorSlot(Player player, EquipmentSlot slot) {
    ItemStack inslot = player.getInventory().armor.get(slot.getIndex());
    Item item = (inslot.isEmpty()) ? null : inslot.getItem();
    return item;
  }

  /**
   * Teleport player to a specific location in a different dimension
   *
   * Also see EndPortalBlock.java  getPortalDestination
   * and PlayerList.java respawn
   *
   *
   * @param player
   * @return optional vec3 respawn position
   */
  public static Optional<Vec3> getPlayerHome(ServerPlayer player) {
    if (player.getRespawnPosition() == null) { // TODO: is this redundant? player method has its own null check
      return Optional.empty();
    }
    DimensionTransition dt = player.findRespawnPositionAndUseSpawnBlock(false, DimensionTransition.DO_NOTHING);
    return Optional.of(dt.pos());
  }
}
