package com.lothrazar.library.render.type;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

/**
 * https://github.com/Lothrazar/RootsClassic
 *
 * In 1.21.1, the ParticleEngine calls Tesselator.begin(Mode, Format) BEFORE
 * calling type.begin(buffer, textureManager), so begin() only sets up GL state.
 * end() must call BufferUploader.drawWithShader(tessellator.end()) since
 * Tesselator.end() now returns MeshData instead of uploading directly.
 *
 * see other Render Types in this library or build your own
 */
@Deprecated
public class ParticleRenderTypes {

  public static final ParticleRenderType MAGIC_RENDER = new ParticleRenderType() {

    @SuppressWarnings("deprecation")
    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.enableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.depthMask(false);
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "rootsclassic:magic"; // TODO: refactor or remove this maybe
    }
  };
}
