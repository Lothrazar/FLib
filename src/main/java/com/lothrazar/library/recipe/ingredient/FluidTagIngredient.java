package com.lothrazar.library.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

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
    TagKey<Fluid> key = TagKey.create(Registries.FLUID, ResourceLocation.parse(tag));
    return BuiltInRegistries.FLUID.getTag(key)
        .map(set -> set.stream().map(Holder::value).collect(java.util.stream.Collectors.toList()))
        .orElseGet(List::of);
  }

  /**
   * create fluidstacks for all fluids matching the tag. if hastag
   * 
   * @return
   */
  public List<FluidStack> getMatchingFluids() {
    List<Fluid> fluids = list();
    List<FluidStack> me = new ArrayList<>();
    for (Fluid f : fluids) {
      me.add(new FluidStack(f, this.amount));
    }
    return me;
  }

  public static FluidTagIngredient readFromPacket(FriendlyByteBuf buffer) {
    ResourceLocation fluidId = buffer.readResourceLocation();
    int fluidAmount = buffer.readInt();
    String tagStr = buffer.readUtf();
    int cnt = buffer.readInt();
    Fluid fluid = BuiltInRegistries.FLUID.getOptional(fluidId).orElse(Fluids.EMPTY);
    FluidStack stack = fluid == Fluids.EMPTY ? FluidStack.EMPTY : new FluidStack(fluid, fluidAmount);
    return new FluidTagIngredient(stack, tagStr, cnt);
  }

  public void writeToPacket(FriendlyByteBuf buffer) {
    ResourceKey<Fluid> key = BuiltInRegistries.FLUID.getResourceKey(fluid.getFluid())
        .orElseThrow(() -> new IllegalStateException("Unknown fluid: " + fluid.getFluid()));
    buffer.writeResourceLocation(key.location());
    buffer.writeInt(fluid.getAmount());
    buffer.writeUtf(tag);
    buffer.writeInt(amount);
  }

  public int getAmount() {
    return amount;
  }
}
