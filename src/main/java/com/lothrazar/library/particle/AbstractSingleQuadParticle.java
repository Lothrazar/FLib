package com.lothrazar.library.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * used by ParticleCasting
 *
 * 26.1 port: SingleQuadParticle (the TextureSheetParticle replacement) now requires its
 * TextureAtlasSprite at construction time instead of binding an arbitrary texture per-frame in
 * render() - there's no more ParticleRenderType.CUSTOM escape hatch, every particle now goes
 * through the atlas + Layer system. This means getTexture() is resolved through the particles
 * atlas (assets/<ns>/textures/particle/... must be reachable via the particles sprite source,
 * same as any vanilla particle texture) rather than bound directly, and subclasses must pass
 * their texture identifier into the super constructor instead of only overriding getTexture().
 *
 * @author lothr
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractSingleQuadParticle extends SingleQuadParticle {

  private final Identifier texture;
  private final SingleQuadParticle.Layer layer;

  protected AbstractSingleQuadParticle(ClientLevel world, double x, double y, double z, Identifier texture) {
    super(world, x, y, z, spriteFor(texture));
    this.texture = texture;
    this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
  }

  protected AbstractSingleQuadParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, Identifier texture) {
    super(world, x, y, z, motionX, motionY, motionZ, spriteFor(texture));
    this.texture = texture;
    this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
  }

  private static TextureAtlasSprite spriteFor(Identifier texture) {
    return Minecraft.getInstance().getAtlasManager().get(new SpriteId(AtlasIds.PARTICLES, texture));
  }

  public Identifier getTexture() {
    return texture;
  }

  @Override
  protected SingleQuadParticle.Layer getLayer() {
    return layer;
  }
}
