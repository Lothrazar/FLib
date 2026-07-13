package com.lothrazar.library.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Used by ParticleBlinkingAura, ParticleGhost, ParticleGraveSmoke, ParticleRotatingSmoke
 *
 * 26.1 port: TextureSheetParticle is gone; SingleQuadParticle is the replacement and now takes
 * its TextureAtlasSprite directly in the constructor (matching vanilla's own SpriteSet-driven
 * particles, e.g. SuspendedParticle) - the owning ParticleProvider is expected to pick the sprite
 * from its SpriteSet and pass it in, rather than the particle picking its own sprite later.
 */
@OnlyIn(Dist.CLIENT)
public class TransparentParticle extends SingleQuadParticle {

  protected TransparentParticle(ClientLevel world, double x, double y, double z, TextureAtlasSprite sprite) {
    super(world, x, y, z, sprite);
  }

  @Override
  protected SingleQuadParticle.Layer getLayer() {
    return SingleQuadParticle.Layer.TRANSLUCENT;
  }
}
