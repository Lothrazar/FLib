package com.lothrazar.library.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FluidTagIngredient {

  private final FluidStack fluid;
  private final String tag;
  private final int amount; // copy of fluidstack count used for tags

  public FluidTagIngredient(FluidStack fs, String str, int cnt) {
    fluid = (fs == null) ? FluidStack.EMPTY : fs;
    tag = (str == null) ? "" : str;
    amount = cnt;
  }

  public FluidStack getFluidStack() {
    return fluid;
  }

  public String getTag() {
    return tag;
  }

  public boolean hasFluid() {
    return !fluid.isEmpty();
  }

  public boolean hasTag() {
    return !tag.isEmpty();
  }

  public List<Fluid> list() {
    if (!hasTag()) {
      return List.of(fluid.getFluid());
    }
    TagKey<Fluid> ft = FluidTags.create(ResourceLocation.parse(tag));
    return BuiltInRegistries.FLUID.getTag(ft).map(named -> named.stream().map(net.minecraft.core.Holder::value).toList()).orElse(List.of());
  }

  public List<FluidStack> getMatchingFluids() {
    List<Fluid> fluids = list();
    List<FluidStack> me = new ArrayList<>();
    for (Fluid f : fluids) {
      me.add(new FluidStack(f, this.amount));
    }
    return me;
  }

  public static final com.mojang.serialization.MapCodec<FluidTagIngredient> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
      FluidStack.CODEC.optionalFieldOf("fluid", FluidStack.EMPTY).forGetter(FluidTagIngredient::getFluidStack),
      com.mojang.serialization.Codec.STRING.optionalFieldOf("tag", "").forGetter(FluidTagIngredient::getTag),
      com.mojang.serialization.Codec.INT.optionalFieldOf("count", 1000).forGetter(FluidTagIngredient::getAmount)
  ).apply(instance, FluidTagIngredient::new));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, FluidTagIngredient> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
      FluidStack.STREAM_CODEC, i -> i.fluid,
      net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8, i -> i.tag,
      net.minecraft.network.codec.ByteBufCodecs.INT, i -> i.amount,
      FluidTagIngredient::new
  );

  public static FluidTagIngredient readFromPacket(net.minecraft.network.RegistryFriendlyByteBuf buffer) {
    return STREAM_CODEC.decode(buffer);
  }

  public void writeToPacket(net.minecraft.network.RegistryFriendlyByteBuf buffer) {
    STREAM_CODEC.encode(buffer, this);
  }

  public int getAmount() {
    return amount;
  }
}
