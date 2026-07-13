package com.lothrazar.library.render.type;

import com.lothrazar.library.FutureLibMod;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

/**
 * Renders the cube outline of the block, just all the edges not the faces
 *
 * 26.1 port: see LaserRenderType for the general RenderPipeline/RenderSetup migration notes.
 * Line width (was a static LineStateShard) is now supplied per-vertex via VertexConsumer#setLineWidth.
 */
public class LineRenderType {

  // Like RenderPipelines.LINES_SNIPPET, but with depth testing disabled so the outline is always visible.
  private static final RenderPipeline TOMB_LINES_PIPELINE = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/line_tomb_lines"))
      .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
      .build();

  public static RenderType tombLinesType() {
    return RenderType.create("flib:tomb_lines",
        RenderSetup.builder(TOMB_LINES_PIPELINE)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
            .createRenderSetup());
  }

  public static void registerPipelines(RegisterRenderPipelinesEvent event) {
    event.registerPipeline(TOMB_LINES_PIPELINE);
  }
}
