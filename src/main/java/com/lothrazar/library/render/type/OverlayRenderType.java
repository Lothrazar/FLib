package com.lothrazar.library.render.type;

import java.util.Optional;
import com.lothrazar.library.FutureLibMod;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

/**
 * Copyright (c) 2015 Vorquel (modified by Lothrazar 2016-2023)
 *
 * This software is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any damages arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose, including commercial applications, and to alter it and redistribute it freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 *
 * 2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
 *
 * 3. This notice may not be removed or altered from any source distribution.
 *
 * ********
 *
 * Renders a texture overlayed onto one face of a block
 *
 * 26.1 port: only the RenderPipeline (shader/blend/format) needs to be registered ahead of time via
 * RegisterRenderPipelinesEvent - the actual texture binding lives on the per-call RenderSetup, so
 * this can stay a runtime factory taking an arbitrary Identifier like before.
 */
public class OverlayRenderType {

  private static final RenderPipeline OVERLAY_PIPELINE = RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
      .withLocation(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "pipeline/overlay"))
      .withVertexShader("core/rendertype_text")
      .withFragmentShader("core/rendertype_text")
      .withSampler("Sampler0")
      .withCull(false)
      .withDepthStencilState(Optional.empty())
      .build();

  public static RenderType overlayRenderer(String id, Identifier resourceLocation) {
    // OutputTarget no longer has a dedicated TRANSLUCENT_TARGET (only MAIN_TARGET/OUTLINE_TARGET/
    // WEATHER_TARGET/ITEM_ENTITY_TARGET) - defaulting to MAIN_TARGET, which is also the RenderSetup
    // builder's own default if left unset.
    return RenderType.create(id,
        RenderSetup.builder(OVERLAY_PIPELINE)
            .withTexture("Sampler0", resourceLocation)
            .setOutputTarget(OutputTarget.MAIN_TARGET)
            .createRenderSetup());
  }

  public static void registerPipelines(RegisterRenderPipelinesEvent event) {
    event.registerPipeline(OVERLAY_PIPELINE);
  }
}
