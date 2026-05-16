package com.lothrazar.library.recipe.ingredient;

import com.google.gson.JsonObject;
import com.lothrazar.library.FutureLibMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class RandomizedOutputIngredient {

  private static final String KEY_PERCENT = "percent";
  private static final String KEY_BONUS = "bonus";
  public ItemStack bonus = ItemStack.EMPTY;
  public int percent;

  public RandomizedOutputIngredient(JsonObject json) {
    parseData(json);
  }

  public RandomizedOutputIngredient(int readInt, ItemStack readItem) {
    this.percent = readInt;
    this.bonus = readItem;
  }

  public static final Codec<RandomizedOutputIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.INT.optionalFieldOf("percent", 0).forGetter(r -> r.percent),
      ItemStack.OPTIONAL_CODEC.optionalFieldOf("bonus", ItemStack.EMPTY).forGetter(r -> r.bonus)
  ).apply(instance, RandomizedOutputIngredient::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, RandomizedOutputIngredient> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, r -> r.percent,           // VAR_INT — percent is 0..100, 1 byte vs 4
      ItemStack.OPTIONAL_STREAM_CODEC, r -> r.bonus,
      RandomizedOutputIngredient::new
  );

  private void parseData(JsonObject json) {
    if (json.has(KEY_BONUS) && json.has(KEY_PERCENT)) {

      //bonus =  ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, KEY_BONUS));
      // replaced with codec parse
      bonus = ItemStack.CODEC.parse(JsonOps.INSTANCE, json)
        .resultOrPartial(errorMessage -> {
          FutureLibMod.LOGGER.error("Failed to parse ItemStack: {}", errorMessage);
        })
        .orElse(ItemStack.EMPTY);

      percent = json.get(KEY_PERCENT).getAsInt();
      percent = Math.max(0, percent);
      if (percent > 100) {
        percent = 100;
      }
    }
  }
}
