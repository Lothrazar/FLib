package com.lothrazar.library.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lothrazar.library.recipe.ingredient.FluidTagIngredient;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class RecipeUtil {
  /**
   * @Deprecated, as this is no longer required because a version has been merged into neoforge.
   *
   *  @code SizedFluidIngredient.NESTED_CODEC
   *  @see net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
   */
  @Deprecated(forRemoval = true, since = "1.21.1-0.2.0")
  public static boolean matchFluid(FluidStack tileFluid, FluidTagIngredient ing) {

    return false;
  }
  /**
   * @Deprecated, as this is no longer required because a version has been merged into neoforge.
   *
   *  @code SizedFluidIngredient.NESTED_CODEC
   *  @see net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
   */
  @Deprecated(forRemoval = true, since = "1.21.1-0.2.0")
  public static FluidTagIngredient parseFluid(JsonObject json, String key) {
    JsonObject mix = json.get(key).getAsJsonObject();
    int count = mix.get("count").getAsInt();
    if (count < 1) {
      count = 1;
    }
    FluidStack fluidstack = FluidStack.EMPTY;
    if (mix.has("fluid")) {
      String fluidId = mix.get("fluid").getAsString(); // JSONUtils.getString(mix, "fluid");
      ResourceLocation resourceLocation = ResourceLocation.parse(fluidId);
      Fluid fluid = BuiltInRegistries.FLUID.getOptional(resourceLocation).orElse(null);
      fluidstack = (fluid == null) ? FluidStack.EMPTY : new FluidStack(fluid, count);
    }
    String ftag = mix.has("tag") ? mix.get("tag").getAsString() : "";
    return new FluidTagIngredient(fluidstack, ftag, count);
  }

  public static NonNullList<Ingredient> getIngredientsArray(JsonObject obj) {
    JsonArray array = GsonHelper.getAsJsonArray(obj, "ingredients");
    NonNullList<Ingredient> nonnulllist = NonNullList.create();
    for (int i = 0; i < array.size(); ++i) {
      Ingredient ingredient = Ingredient.CODEC.parse(JsonOps.INSTANCE, array.get(i)).result().orElse(Ingredient.EMPTY);
      if (!ingredient.isEmpty()) {
        nonnulllist.add(ingredient);
      }
    }
    return nonnulllist;
  }

  public static FluidStack getFluid(JsonObject fluidJson) {
    if (fluidJson.has("fluidTag")) {
      //      String fluidTag = fluidJson.get("fluidTag").getAsString();
    }
    String fluidId = GsonHelper.getAsString(fluidJson, "fluid");
    ResourceLocation resourceLocation = ResourceLocation.parse(fluidId);
    Fluid fluid = BuiltInRegistries.FLUID.getOptional(resourceLocation).orElse(null);
    int count = fluidJson.get("count").getAsInt();
    if (count < 1) {
      count = 1;
    }
    return new FluidStack(fluid, count);
  }
}
