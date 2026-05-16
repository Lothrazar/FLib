package com.lothrazar.library.recipe.ingredient;

import com.google.gson.JsonObject;
import com.lothrazar.library.FutureLibMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class EnergyIngredient {

  private static final String KEY_ENERGY = "energy";
  private static final int TICKS_DEFAULT = 60;
  private static final int RFPT_DEFAULT = 80;
  private int rfPertick;
  private int ticks;

  public EnergyIngredient(int rf, int ticks) {
    setRf(rf);
    setTicks(ticks);
  }

  public EnergyIngredient(final JsonObject recipeJson) {
    parseData(recipeJson);
  }


  public static final MapCodec<EnergyIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.INT.optionalFieldOf("rfpertick", RFPT_DEFAULT).forGetter(EnergyIngredient::getRfPertick),
      Codec.INT.optionalFieldOf("ticks", TICKS_DEFAULT).forGetter(EnergyIngredient::getTicks)
  ).apply(instance, EnergyIngredient::new));

  public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, EnergyIngredient> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
      net.minecraft.network.codec.ByteBufCodecs.INT, EnergyIngredient::getRfPertick,
      net.minecraft.network.codec.ByteBufCodecs.INT, EnergyIngredient::getTicks,
      EnergyIngredient::new
  );

  private void parseData(final JsonObject recipeJson) {
    if (!recipeJson.has(KEY_ENERGY)) {
      FutureLibMod.LOGGER.error("  Missing JSON 'energy', setting default values" + recipeJson);
      setRf(RFPT_DEFAULT);
      setTicks(TICKS_DEFAULT);
    }
    else if (recipeJson.get(KEY_ENERGY).isJsonObject()) {
      JsonObject energyJson = recipeJson.get(KEY_ENERGY).getAsJsonObject();
      setRf(energyJson.get("rfpertick").getAsInt());
      setTicks(energyJson.get("ticks").getAsInt());
    }
  }

  private void setTicks(int ticks) {
    this.ticks = Math.max(1, ticks); // at least 1 per operation
  }

  private void setRf(int rf) {
    this.rfPertick = Math.max(0, rf); // not negative, can be zero for free cost
  }

  public int getEnergyTotal() {
    return rfPertick * ticks;
  }

  public int getTicks() {
    return ticks;
  }

  public int getRfPertick() {
    return this.rfPertick;
  }
}
