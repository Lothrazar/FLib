package com.lothrazar.library.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * used by ParticleCasting
 * 
 * @author lothr
 *
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractSingleQuadParticle extends SingleQuadParticle {

  protected AbstractSingleQuadParticle(ClientLevel world, double x, double y, double z) {
    super(world, x, y, z);
  }

  protected AbstractSingleQuadParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
    super(world, x, y, z, motionX, motionY, motionZ);
  }

  public abstract ResourceLocation getTexture();

  @Override
  public void render(VertexConsumer buffer, Camera entityIn, float partialTicks) {
    TextureManager textureManager = Minecraft.getInstance().textureManager;
    //    Lighting.turnOff();
    RenderSystem.depthMask(false);
    textureManager.bindForSetup(getTexture());
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    //    RenderSystem.alphaFunc(516, 0.003921569F);
    //    Tesselator.getInstance().getBuilder().begin(7, DefaultVertexFormat.PARTICLE);
    RenderSystem.setShader(GameRenderer::getParticleShader);
    RenderSystem.setShaderTexture(0, getTexture());
    Tesselator.getInstance().getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE); // QUADS == tuess
    super.render(buffer, entityIn, partialTicks);
    Tesselator.getInstance().end();
  }

  @Override
  protected float getU0() {
    return 0f;
  }

  @Override
  protected float getU1() {
    return 1f;
  }

  @Override
  protected float getV0() {
    return 0f;
  }

  @Override
  protected float getV1() {
    return 1f;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.CUSTOM;
  }
}
