package com.lothrazar.library.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * JSON methods for recipes no longer exists, we can now use built in CODECs or build custom
 *
 *  @see net.minecraft.world.item.ItemStack.CODEC;
 *  @see net.minecraft.world.item.crafting.Ingredient.CODEC
 *  @see net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
 */
@Deprecated(forRemoval = true, since = "1.21.1-0.2.0")
public class RecipeUtil {
}
