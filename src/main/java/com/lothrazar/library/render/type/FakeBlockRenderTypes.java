package com.lothrazar.library.render.type;

import java.util.Optional;
import com.lothrazar.library.FutureLibMod;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

/**
 * Render Types with help from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
 *
 * 26.1 port: see LaserRenderType for the general RenderPipeline/RenderSetup migration notes.
 * The old ADDITIVE_TRANSPARENCY shard is now just BlendFunction.ADDITIVE. The old distinction
 * between COLOR_WRITE (no depth write) and COLOR_DEPTH_WRITE (write depth without testing it) is
 * expressed via DepthStencilState: Optional.empty() for no depth attachment at all versus
 * new DepthStencilState(CompareOp.ALWAYS_PASS, true) for "always pass the test but still write".
 */
public class FakeBlockRenderTypes {

  public static final Identifier BEAM = Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "textures/effect/beam.png");

  private static final RenderPipeline BEAM_PIPELINE = RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/fakeblock_beam"))
      .withVertexShader("core/rendertype_text")
      .withFragmentShader("core/rendertype_text")
      .withSampler("Sampler0")
      .withCull(false)
      .withDepthStencilState(Optional.empty())
      .build();

  public static final RenderType LASER_MAIN_BEAM = RenderType.create("flib:mininglasermainbeam",
      RenderSetup.builder(BEAM_PIPELINE)
          .withTexture("Sampler0", BEAM)
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .createRenderSetup());

  private static final RenderPipeline FAKE_BLOCK_PIPELINE = RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/fakeblock"))
      .withVertexShader("core/rendertype_text")
      .withFragmentShader("core/rendertype_text")
      .withSampler("Sampler0")
      .withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
      .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
      .build();

  /**
   * used by TESR that render blocks with textures Shape builder, ghostsoundmuffler, render light camo.
   */
  public static final RenderType FAKE_BLOCK = RenderType.create("flib:fakeblock",
      RenderSetup.builder(FAKE_BLOCK_PIPELINE)
          .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .createRenderSetup());

  private static final RenderPipeline SOLID_COLOUR_PIPELINE = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/solid_colour"))
      .withVertexShader("core/position_color")
      .withFragmentShader("core/position_color")
      .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
      .withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
      .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
      .build();

  /**
   * used by EventRender -> RenderWorldLastEvent by most held items that pick locations, such as cyclic:location_data
   */
  public static final RenderType TRANSPARENT_COLOUR = RenderType.create("flib:transparentcolour",
      RenderSetup.builder(SOLID_COLOUR_PIPELINE)
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
          .createRenderSetup());

  /**
   * used by most blocks that select blocks such as cyclic:forester, cyclic:harvester, cyclic:miner in TESRs
   */
  public static final RenderType SOLID_COLOUR = RenderType.create("flib:solidcolour",
      RenderSetup.builder(SOLID_COLOUR_PIPELINE)
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
          .createRenderSetup());

  // Like RenderPipelines.LINES_SNIPPET, but with depth testing disabled so the outline is always visible.
  private static final RenderPipeline NO_DEPTH_LINES_PIPELINE = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/tomb_lines"))
      .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
      .build();

  /**
   * Used by cyclic:prospector
   */
  public static final RenderType TOMB_LINES = RenderType.create("flib:tomb_lines",
      RenderSetup.builder(NO_DEPTH_LINES_PIPELINE)
          .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
          .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
          .createRenderSetup());

  public static void registerPipelines(RegisterRenderPipelinesEvent event) {
    event.registerPipeline(BEAM_PIPELINE);
    event.registerPipeline(FAKE_BLOCK_PIPELINE);
    event.registerPipeline(SOLID_COLOUR_PIPELINE);
    event.registerPipeline(NO_DEPTH_LINES_PIPELINE);
  }
}
