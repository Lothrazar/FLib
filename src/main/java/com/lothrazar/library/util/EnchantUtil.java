package com.lothrazar.library.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;

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
}
