package com.lothrazar.library.registry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.cauldron.CauldronInteraction;
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

  public static void addWater(ItemLike input, ItemLike output) {
    addWater(new RecipeCauldronFactory.CauldronFakeRecipe(input, output));
  }

  public static void addWater(ItemLike input, ItemLike output, boolean lowerFillLevel, int consume) {
    addWater(new RecipeCauldronFactory.CauldronFakeRecipe(input, output, lowerFillLevel, consume));
  }

  public static void addWater(CauldronFakeRecipe rec) {
    WATERLIST.add(rec);
  }

  /**
   * If your mod adds recipes, this must be called
   * 
   * @param event
   */
  public static void setup(FMLCommonSetupEvent event) {
    // TODO: 1.21 CauldronInteraction migration
  }
}
