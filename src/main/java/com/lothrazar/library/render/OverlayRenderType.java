package com.lothrazar.library.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

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
 * 
 */
public class OverlayRenderType extends RenderType {

  public OverlayRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  public static RenderType overlayRenderer(String id, ResourceLocation resourceLocation) {
    RenderType.CompositeState state = RenderType.CompositeState.builder()
        .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
        .setCullState(RenderStateShard.NO_CULL)
        .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
        .createCompositeState(true);
    return create(id, DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, true, false, state);
  }
}
