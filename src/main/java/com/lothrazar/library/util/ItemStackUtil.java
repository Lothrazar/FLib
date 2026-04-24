package com.lothrazar.library.util;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.*;

public class ItemStackUtil {

  public static final String NBT_LORE = "Lore";
  public static final String NBT_DISPLAY = "display";

  public static void addLoreToStack(ItemStack crafting, String lore, String color) {
    net.minecraft.world.item.component.ItemLore itemLore = crafting.getOrDefault(net.minecraft.core.component.DataComponents.LORE, net.minecraft.world.item.component.ItemLore.EMPTY);
    java.util.List<net.minecraft.network.chat.Component> newLore = new java.util.ArrayList<>(itemLore.lines());
    newLore.add(net.minecraft.network.chat.Component.literal(lore).withStyle(net.minecraft.ChatFormatting.getByCode(color.charAt(0))));
    crafting.set(net.minecraft.core.component.DataComponents.LORE, new net.minecraft.world.item.component.ItemLore(newLore));
  }

  public static void applyRandomEnch(RandomSource random, ItemStack crafting) {
    // TODO: 1.21 EnchantmentHelper needs RegistryAccess
  }

  public static void applyRandomEnch(RandomSource random, ItemStack crafting, int level, boolean allowTreasure) {
    applyRandomEnch(random, crafting);
  }

  public static int countEmptySlots(IItemHandler handler) {
    if (handler == null) {
      return 0;
    }
    int empty = 0;
    for (int i = 0; i < handler.getSlots(); i++) {
      if (handler.getStackInSlot(i).isEmpty()) {
        empty++;
      }
    }
    return empty;
  }

  public static ItemStack findItem(String id) {
    Item head = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(id));
    if (head != null) {
      return new ItemStack(head);
    }
    return ItemStack.EMPTY;
  }

  public static void dropAll(IItemHandler items, Level world, BlockPos pos) {
    if (items == null) {
      return;
    }
    for (int i = 0; i < items.getSlots(); i++) {
      Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
    }
  }

  public static void repairItem(ItemStack s) {
    repairItem(s, 1);
  }

  public static void repairItem(ItemStack s, int amount) {
    s.setDamageValue(Math.max(0, s.getDamageValue() - amount));
  }

  public static void damageItem(LivingEntity player, ItemStack stack) {
    damageItem(player, stack, InteractionHand.MAIN_HAND);
  }

  public static void damageItem(LivingEntity player, ItemStack stack, InteractionHand hand) {
    if (!stack.isDamageableItem()) {
      return;
    }
    stack.setDamageValue(stack.getDamageValue() + 1);
    if (stack.getDamageValue() >= stack.getMaxDamage()) {
      stack.shrink(1);
    }
  }

  public static void damageItemRandomly(LivingEntity player, ItemStack stack) {
    if (player.level().random.nextDouble() < 0.001) {
      damageItem(player, stack);
    }
  }

  public static void drop(Level world, BlockPos pos, Block drop) {
    if (!world.isClientSide) {
      world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(drop.asItem())));
    }
  }

  public static void drop(Level world, BlockPos pos, ItemStack drop) {
    if (!world.isClientSide) {
      world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
    }
  }

  public static boolean matches(ItemStack current, ItemStack in) {
    return ItemStack.matches(current, in)
        && ItemStack.isSameItemSameComponents(current, in);
  }

  public static void shrink(Player player, ItemStack stac) {
    if (!player.isCreative()) {
      stac.shrink(1);
    }
  }

  public static void drop(Level world, BlockPos center, List<ItemStack> lootDrops) {
    for (ItemStack dropMe : lootDrops) {
      ItemStackUtil.drop(world, center, dropMe);
    }
  }

  public static void dropItemStackMotionless(Level world, BlockPos pos, ItemStack stack) {
    if (stack.isEmpty()) {
      return;
    }
    if (world.isClientSide == false) {
      ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
      world.addFreshEntity(entityItem);
      entityItem.setDeltaMovement(0, 0, 0);
    }
  }

  public static void deleteTag(ItemStack itemstack) {
    itemstack.remove(com.lothrazar.library.registry.FlibDataComponents.CUSTOM_NBT_BUCKET.get());
  }

  public static void randomlyRepair(RandomSource rnd, ItemStack stack, int factor) {
    if (stack.isDamaged() && rnd.nextInt(factor) == 0) {
      stack.setDamageValue(stack.getDamageValue() - 1);
    }
  }
}
