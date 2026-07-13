package com.lothrazar.library.render;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Source from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
 * <p>
 * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
 * <p>
 * Map which uses FluidStacks as keys, ignoring amount. Primary use: caching FluidStack aware fluid rendering (NBT, yay)
 */
@SuppressWarnings("serial")
public class FluidRenderMap<V> extends Object2ObjectOpenCustomHashMap<FluidStack, V> {

  public enum FluidFlow {
    STILL, FLOWING
  }

  public FluidRenderMap() {
    super(FluidHashStrategy.INSTANCE);
  }

  // 26.1 port: IClientFluidTypeExtensions#getStillTexture/getFlowingTexture were removed. Fluid
  // sprites are now resolved through the fluid's baked FluidModel (same object the vanilla
  // in-world FluidRenderer uses), fetched from Minecraft's ModelManager.
  public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, FluidFlow type) {
    FluidState fluidState = fluidStack.getFluid().defaultFluidState();
    FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
    return type == FluidFlow.STILL ? model.stillMaterial().sprite() : model.flowingMaterial().sprite();
  }

  public static TextureAtlasSprite getSprite(Identifier spriteLocation) {
    return Minecraft.getInstance().getAtlasManager().get(new SpriteId(AtlasIds.BLOCKS, spriteLocation));
  }

  /**
   * Implements equals & hashCode that ignore FluidStack#amount
   */
  public static class FluidHashStrategy implements Hash.Strategy<FluidStack> {

    public static FluidHashStrategy INSTANCE = new FluidHashStrategy();

    @Override
    public int hashCode(FluidStack stack) {
      if (stack == null || stack.isEmpty()) {
        return 0;
      }
      int code = 1;
      code = 31 * code + stack.getFluid().hashCode();
      if (!stack.getComponents().isEmpty()) {
        code = 31 * code + stack.getComponents().hashCode();
      }
      return code;
    }

    @Override
    public boolean equals(FluidStack a, FluidStack b) {
      return a == null ? b == null : b != null && FluidStack.isSameFluidSameComponents(a, b);
    }
  }
}
