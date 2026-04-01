package com.lothrazar.library.render.type;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
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
 */
public class ParticleRenderTypes {

  public static final ParticleRenderType MAGIC_RENDER = new ParticleRenderType() {

    @Override
    public void begin(BufferBuilder buffer, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.enableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.depthMask(false);
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      // Note: do NOT call buffer.begin() here - the ParticleEngine already started
      // the buffer via Tesselator.begin(Mode, Format) before calling this method.
    }

    @Override
    public void end(Tesselator tessellator) {
      MeshData meshData = tessellator.end();
      if (meshData != null) {
        BufferUploader.drawWithShader(meshData);
      }
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableCull();
    }

    @Override
    public String toString() {
      return "rootsclassic:magic";
    }
  };
}
