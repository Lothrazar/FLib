package com.lothrazar.library.recipe;

import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;

public class BrewingRecipeFlib extends BrewingRecipe {

  private ItemStack inputStack;

  public BrewingRecipeFlib(ItemStack inputStack, Ingredient ingredient, ItemStack output) {
    super(Ingredient.of(inputStack), ingredient, output);
    this.inputStack = inputStack;
  }

  @Override
  public boolean isInput(ItemStack stack) {
    return super.isInput(stack) && PotionContents.getPotion(stack) == PotionContents.getPotion(inputStack);
  }
}
