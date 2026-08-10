package com.lothrazar.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class EnchantUtil {

  public static List<MobEffect> getNegativeEffects() {
    return getEffects(MobEffectCategory.HARMFUL);
  }

  public static List<MobEffect> getBeneficialEffects() {
    return getEffects(MobEffectCategory.BENEFICIAL);
  }

  public static List<MobEffect> getNeutralEffects() {
    return getEffects(MobEffectCategory.NEUTRAL);
  }

  public static List<MobEffect> getAllEffects() {
    return getEffects(null);
  }

  public static List<MobEffect> getEffects(MobEffectCategory effectType) {
    List<MobEffect> effectsList = new ArrayList<>();
    for (MobEffect effect : BuiltInRegistries.MOB_EFFECT) {
      if (effectType == null || effect.getCategory() == effectType) {
        effectsList.add(effect);
      }
    }
    return effectsList;
  }

  public static boolean doBookEnchantmentsMatch(ItemStack stack1, ItemStack stack2) {
    Item item1 = stack1.getItem();
    Item item2 = stack2.getItem();
    if (item1 == Items.ENCHANTED_BOOK && item2 == Items.ENCHANTED_BOOK) {
      ItemEnchantments ench1 = stack1.get(DataComponents.STORED_ENCHANTMENTS);
      ItemEnchantments ench2 = stack2.get(DataComponents.STORED_ENCHANTMENTS);
      if (ench1 == null || ench2 == null) {
        return false;
      }
      if (ench1.equals(ench2)) {
        return true;
      }
    }
    return false;
  }

  public static Holder<Enchantment> holder(ResourceKey<Enchantment> key, Level level) {
    try {
      var registry = level.registryAccess().lookup(Registries.ENCHANTMENT);
      if (registry.isEmpty()) {
        return null;
      }
      var holder = registry.get().get(key);
      if (holder.isEmpty()) {
        return null;
      }
      return holder.get();
    } catch (Exception e) {
      return null;
    }
  }

  public static Holder<Enchantment> holder(ResourceKey<Enchantment> key, LivingEntity entity) {
    return holder(key, entity.level());
  }

  public static int getCurrentLevelTool(Holder<Enchantment> enchantment, ItemStack stack) {
    if (stack.isEmpty() || stack.getItem() == Items.ENCHANTED_BOOK) return -1;
    if (enchantment == null) return -1;
    return EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
  }

  public static int getCurrentArmorLevel(ResourceKey<Enchantment> key, LivingEntity entity) {
    Holder<Enchantment> h = holder(key, entity);
    if (h == null) return 0;
    return getCurrentArmorLevel(h, entity);
  }

  public static int getCurrentArmorLevelSlot(Holder<Enchantment> enchantment, LivingEntity entity, EquipmentSlot type) {
    if (enchantment == null) return 0;
    ItemStack armor = entity.getItemBySlot(type);
    if (armor.isEmpty()) return 0;
    return EnchantmentHelper.getTagEnchantmentLevel(enchantment, armor);
  }

  public static int getCurrentArmorLevel(Holder<Enchantment> enchantment, LivingEntity entity) {
    if (enchantment == null) return 0;
    EquipmentSlot[] armors = { EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS };
    int level = 0;
    for (EquipmentSlot slot : armors) {
      int slotLevel = getCurrentArmorLevelSlot(enchantment, entity, slot);
      if (slotLevel > level) level = slotLevel;
    }
    return level;
  }

  public static int getLevelAll(Holder<Enchantment> enchantment, LivingEntity entity) {
    if (enchantment == null) return 0;
    return Math.max(getCurrentArmorLevel(enchantment, entity), getCurrentLevelTool(enchantment, entity));
  }

  public static ItemStack getFirstArmorStackWithEnchant(Holder<Enchantment> enchantment, LivingEntity entity) {
    if (entity == null || enchantment == null) return ItemStack.EMPTY;
    for (ItemStack armor : entity.getArmorSlots()) {
      if (!armor.isEmpty() && EnchantmentHelper.getTagEnchantmentLevel(enchantment, armor) > 0) {
        return armor;
      }
    }
    return ItemStack.EMPTY;
  }

  public static int getCurrentLevelTool(Holder<Enchantment> enchantment, LivingEntity entity) {
    if (entity == null || enchantment == null) return -1;
    ItemStack main = entity.getMainHandItem();
    ItemStack off = entity.getOffhandItem();
    return Math.max(getCurrentLevelTool(enchantment, main), getCurrentLevelTool(enchantment, off));
  }
}
