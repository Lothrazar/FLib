package com.lothrazar.library.registry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class RecipeCauldronFactory {

  public static class CauldronFakeRecipe {

    public ItemLike input;
    public ItemLike output;
    public boolean lowerFillLevel;
    public int bonus = 0;

    public CauldronFakeRecipe(ItemLike input, ItemLike output) {
      this(input, output, true, 0);
    }

    public CauldronFakeRecipe(ItemLike input, ItemLike output, boolean lowerFillLevel, int bonus) {
      super();
      this.input = input;
      this.output = output;
      this.lowerFillLevel = lowerFillLevel;
      this.bonus = bonus;
    }
  }

  private static List<CauldronFakeRecipe> WATERLIST = new ArrayList<>();
  private static List<CauldronFakeRecipe> LAVALIST = new ArrayList<>();

  public static void addWater(ItemLike input, ItemLike output) {
    addWater(new RecipeCauldronFactory.CauldronFakeRecipe(input, output));
  }

  public static void addWater(ItemLike input, ItemLike output, boolean lowerFillLevel, int consume) {
    addWater(new RecipeCauldronFactory.CauldronFakeRecipe(input, output, lowerFillLevel, consume));
  }

  public static void addWater(CauldronFakeRecipe rec) {
    WATERLIST.add(rec);
  }
  public static void addLava(CauldronFakeRecipe rec) {
    LAVALIST.add(rec);
  }

  /**
   * Returns a copy of the list, useful for JEI plugins
   */
  public static List<CauldronFakeRecipe> getWaterRecipes() {
    return List.copyOf(WATERLIST);
  }
  public static List<CauldronFakeRecipe> geLavaRecipes() {
    return List.copyOf(LAVALIST);
  }
  /**
   * If your mod adds recipes, this must be called
   * 
   * @param event
   */
  public static void setup(FMLCommonSetupEvent event) {
    for (CauldronFakeRecipe rec : WATERLIST) {
      final CauldronInteraction interaction = (state, level, pos, player, hand, stack) -> {
        if (stack.is(rec.input.asItem())) {
          //replace all the item, be generous. we could instead stack.shrink and drop just one
          player.setItemInHand(hand, new ItemStack(rec.output.asItem(), stack.getCount() + rec.bonus));
          if (rec.lowerFillLevel) {
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
          }
          return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
      };
      CauldronInteractions.WATER.put(rec.input.asItem(), interaction);
    }
    for (CauldronFakeRecipe rec : LAVALIST) {
      final CauldronInteraction interaction = (state,  level, pos, player, hand, stack) -> {
        if (stack.is(rec.input.asItem())) {
          //replace all the item, be generous. we could instead stack.shrink and drop just one
          player.setItemInHand(hand, new ItemStack(rec.output.asItem(), stack.getCount() + rec.bonus));
          if (rec.lowerFillLevel) {
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
          }
          return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
      };
      CauldronInteractions.LAVA.put(rec.input.asItem(), interaction);
    }
  }
}
