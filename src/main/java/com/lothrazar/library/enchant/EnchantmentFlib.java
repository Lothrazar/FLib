package com.lothrazar.library.enchant;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Utility helpers for working with enchantments.
 *
 * In Minecraft 1.21+, enchantments are data-driven and cannot be subclassed directly.
 * Define enchantments as JSON data files and obtain their Holder<Enchantment> via
 * level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(myKey)
 */
public class EnchantmentFlib {

  public static int getCurrentLevelTool(Holder<Enchantment> enchantment, ItemStack stack) {
    if (stack.isEmpty() || stack.getItem() == Items.ENCHANTED_BOOK) return -1;
    return EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
  }

  public static int getCurrentArmorLevelSlot(Holder<Enchantment> enchantment, LivingEntity entity, EquipmentSlot type) {
    ItemStack armor = entity.getItemBySlot(type);
    if (armor.isEmpty()) return 0;
    return EnchantmentHelper.getTagEnchantmentLevel(enchantment, armor);
  }

  public static int getCurrentArmorLevel(Holder<Enchantment> enchantment, LivingEntity entity) {
    EquipmentSlot[] armors = { EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS };
    int level = 0;
    for (EquipmentSlot slot : armors) {
      int slotLevel = getCurrentArmorLevelSlot(enchantment, entity, slot);
      if (slotLevel > level) level = slotLevel;
    }
    return level;
  }

  public static int getLevelAll(Holder<Enchantment> enchantment, LivingEntity entity) {
    return Math.max(getCurrentArmorLevel(enchantment, entity), getCurrentLevelTool(enchantment, entity));
  }

  public static ItemStack getFirstArmorStackWithEnchant(Holder<Enchantment> enchantment, LivingEntity entity) {
    if (entity == null) return ItemStack.EMPTY;
    for (ItemStack armor : entity.getArmorSlots()) {
      if (!armor.isEmpty() && EnchantmentHelper.getTagEnchantmentLevel(enchantment, armor) > 0) {
        return armor;
      }
    }
    return ItemStack.EMPTY;
  }

  public static int getCurrentLevelTool(Holder<Enchantment> enchantment, LivingEntity entity) {
    if (entity == null) return -1;
    ItemStack main = entity.getMainHandItem();
    ItemStack off = entity.getOffhandItem();
    return Math.max(getCurrentLevelTool(enchantment, main), getCurrentLevelTool(enchantment, off));
  }
}
