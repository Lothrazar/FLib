package com.lothrazar.library.recipe.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public class RandomizedOutputIngredient {

  public static final Codec<RandomizedOutputIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.INT.fieldOf("percent").forGetter(i -> i.percent),
      ItemStack.CODEC.optionalFieldOf("bonus", ItemStack.EMPTY).forGetter(i -> i.bonus)
  ).apply(instance, RandomizedOutputIngredient::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, RandomizedOutputIngredient> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT, i -> i.percent,
      ItemStack.STREAM_CODEC, i -> i.bonus,
      RandomizedOutputIngredient::new
  );

  public ItemStack bonus = ItemStack.EMPTY;
  public int percent;

  public RandomizedOutputIngredient(int percent, ItemStack bonus) {
    this.percent = Math.max(0, Math.min(100, percent));
    this.bonus = bonus;
  }
}
