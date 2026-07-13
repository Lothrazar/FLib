package com.lothrazar.library.render.type;

import com.lothrazar.library.FutureLibMod;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

/**
 * Source from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
 * <p>
 * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
 *
 * 26.1 port: see LaserRenderType for the general RenderPipeline/RenderSetup migration notes.
 * The original RENDERTYPE_CUTOUT_SHADER + POSITION_COLOR_TEX_LIGHTMAP combination doesn't map
 * cleanly onto the new pipeline system (vanilla's cutout shader now requires the full BLOCK
 * vertex format). Reusing the text-shader pipeline (translucent, not true alpha-cutout) instead,
 * matching the same simplification already used for FakeBlockRenderTypes.FAKE_BLOCK.
 */
public class FluidTankRenderType {

  private static final RenderPipeline RESIZABLE_PIPELINE = RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/fluidtank_resizable"))
      .withVertexShader("core/rendertype_text")
      .withFragmentShader("core/rendertype_text")
      .withSampler("Sampler0")
      .withSampler("Sampler2")
      .build();

  public static final RenderType RESIZABLE = RenderType.create(FutureLibMod.MODID + ":resizable_cuboid",
      RenderSetup.builder(RESIZABLE_PIPELINE)
          .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
          .useLightmap()
          .createRenderSetup());

  public static void registerPipelines(RegisterRenderPipelinesEvent event) {
    event.registerPipeline(RESIZABLE_PIPELINE);
  }
}
