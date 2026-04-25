package com.lothrazar.library.recipe;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
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
    // PotionUtils.getPotion() removed in 1.21.1 — use DataComponents.POTION_CONTENTS
    PotionContents p1 = stack.get(DataComponents.POTION_CONTENTS);
    PotionContents p2 = inputStack.get(DataComponents.POTION_CONTENTS);
    return super.isInput(stack) && p1 != null && p1.equals(p2);
  }
}
