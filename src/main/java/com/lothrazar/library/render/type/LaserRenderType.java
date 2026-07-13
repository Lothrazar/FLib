package com.lothrazar.library.render.type;

import java.util.Optional;
import com.lothrazar.library.FutureLibMod;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

/**
 * 26.1 port: RenderType is no longer subclassable (its constructor is private) and the old
 * RenderStateShard/CompositeState builder is gone, replaced by RenderPipeline (shader/blend/cull/
 * depth, built once and registered via RegisterRenderPipelinesEvent) + RenderSetup (per-RenderType
 * texture bindings and layering, built from a RenderPipeline). Call {@link #registerPipelines(RegisterRenderPipelinesEvent)}
 * from your mod's client-side RegisterRenderPipelinesEvent listener to make these usable.
 */
public class LaserRenderType {

  private static final Identifier RL_LASER = Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "textures/effect/laser.png");
  private static final Identifier RL_BEAM = Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "textures/effect/beam.png");
  private static final Identifier RL_GLOW = Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "textures/effect/glow.png");

  // Matches the old POSITION_COLOR_TEX_LIGHTMAP + RENDERTYPE_TEXT_SHADER + TRANSLUCENT_TRANSPARENCY +
  // NO_DEPTH_TEST + NO_CULL + LIGHTMAP combination: reuse vanilla's text pipeline shape (same vertex
  // format and shader) but disable depth testing and culling for an unlit, always-visible beam.
  public static final RenderPipeline LASER_PIPELINE = RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/laser_beam"))
      .withVertexShader("core/rendertype_text")
      .withFragmentShader("core/rendertype_text")
      .withSampler("Sampler0")
      .withSampler("Sampler2")
      .withCull(false)
      .withDepthStencilState(Optional.empty())
      .build();

  public static final RenderType LASER_MAIN_BEAM = RenderType.create("flib:main_beam",
      RenderSetup.builder(LASER_PIPELINE)
          .withTexture("Sampler0", RL_BEAM)
          .useLightmap()
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .createRenderSetup());

  public static final RenderType LASER_MAIN_ADDITIVE = RenderType.create("flib:mining_laser_additive_beam",
      RenderSetup.builder(LASER_PIPELINE)
          .withTexture("Sampler0", RL_GLOW)
          .useLightmap()
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .createRenderSetup());

  public static final RenderType LASER_MAIN_CORE = RenderType.create("flib:mining_laser_core_beam",
      RenderSetup.builder(LASER_PIPELINE)
          .withTexture("Sampler0", RL_LASER)
          .useLightmap()
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .createRenderSetup());

  public static void registerPipelines(RegisterRenderPipelinesEvent event) {
    event.registerPipeline(LASER_PIPELINE);
  }
}
