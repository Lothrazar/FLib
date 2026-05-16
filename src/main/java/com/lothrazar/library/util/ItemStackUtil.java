package com.lothrazar.library.util;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemStackUtil {

  public static final String NBT_LORE = "Lore";
  public static final String NBT_DISPLAY = "display";
  //  IItemRenderProperties is IClientBlockExtensions now. 
  //hasContainerItem() is hasCraftingRemainingItem() 
  //and getContainerItem() is getCraftingRemainingItem() now

  /**
   * example
   * 
   * "display": { "Lore": [ "[{\"text\":\"item.enchantingrunes.rune_a\",\"color\":\"gold\"}]" ] },
   * 
   * @param crafting
   * @param lore
   * @param color
   */
  public static void addLoreToStack(ItemStack crafting, String lore, String color) {
    if (color == null) {
      color = "gold";
    }

    var loreDefault = crafting.getOrDefault( DataComponents.LORE, ItemLore.EMPTY);

    List<Component> newLore = new java.util.ArrayList<>(loreDefault.lines());

    newLore.add(Component.literal(lore).withStyle(ChatFormatting.getByName(color))); // TODO: color test
    //then set overwrite
    crafting.set( DataComponents.LORE, new ItemLore(newLore));
//    ChatFormatting fmt = ChatFormatting.getByName(color);
//    Component loreText = fmt != null
//        ? Component.literal(lore).withStyle(fmt)
//        : Component.literal(lore);
//    crafting.set(DataComponents.LORE, new ItemLore(List.of(loreText)));
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
    Item head = BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(id)).orElse(null);
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
      //unbreakable
      return;
    }
    if (player == null) {
      stack.setDamageValue(stack.getDamageValue() + 1);
    }
    else {
      final Item item = stack.getItem();
      stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
//          (p) -> {
//        p.broadcastBreakEvent(EquipmentSlot.MAINHAND);
//      });
    }
    if (stack.getDamageValue() >= stack.getMaxDamage()) {
      stack.shrink(1);
      stack = ItemStack.EMPTY;
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
    //first one fails if size is off
    return ItemStack.isSameItemSameComponents(current, in);
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
      // do not spawn a second 'ghost' one onclient side
      world.addFreshEntity(entityItem);
      entityItem.setDeltaMovement(0, 0, 0);
      //      entityItem.motionX = entityItem.motionY = entityItem.motionZ = 0;
    }
  }

  /**
   * Preserve damage but delete the rest of the tag
   *
   * Its better to not use this shared method and instsead just remove your component one by one
   *
   * example:
   *   itemstack.remove(CUSTOM_NBT_BUCKET.get());
   *
   * @param itemstack
   */
  public static void deleteTag(ItemStack itemstack) {
    int dmg = itemstack.getDamageValue();
    itemstack.remove(DataComponents.CUSTOM_DATA);
    itemstack.setDamageValue(dmg);
  }

  /**
   * call from ::inventoryTick
   * 
   * @param rnd
   * @param stack
   * @param factor
   */
  public static void randomlyRepair(RandomSource rnd, ItemStack stack, int factor) {
    if (stack.isDamaged() && rnd.nextInt(factor) == 0) {
      stack.setDamageValue(stack.getDamageValue() - 1);
    }
  }

  /**
   * true if item is edible. replaces old item.isEdible().  based on FoodProperties
   * @return
   */
  public static boolean isEdible(ItemStack s) {
    return (s.getItem().getFoodProperties(s,null) != null);
  }
}
