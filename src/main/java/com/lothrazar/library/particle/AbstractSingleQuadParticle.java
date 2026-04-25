package com.lothrazar.library.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * used by ParticleCasting
 *
 * @author lothr
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
    // For CUSTOM render type, we set up the texture/blend state then delegate to super.
    // The VertexConsumer writes into the buffer provided by the particle engine.
    RenderSystem.setShaderTexture(0, getTexture());
    RenderSystem.depthMask(false);
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    super.render(buffer, entityIn, partialTicks);
    RenderSystem.depthMask(true);
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
