package com.lothrazar.library.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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
    Collection<MobEffect> effects = net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.stream().toList();
    List<MobEffect> effectsList = new ArrayList<>();
    for (MobEffect effect : effects) {
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

      // TODO: 1.21 Enchantments DataComponents
      return true;
    }
    return false;
  }
}
