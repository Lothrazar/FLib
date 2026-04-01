package com.lothrazar.library.particle.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * ParticleOptions implementation carrying two ints (e.g. two RGB color values).
 * Used by ParticleBlinkingAura and similar effects.
 *
 * <p>In 1.21.1, ParticleOptions no longer has Deserializer, writeToNetwork, or
 * writeToString. Instead, the ParticleType provides a MapCodec and a StreamCodec.
 * Use the static factory methods here when registering your ParticleType: </p>
 *
 * <pre>
 * PARTICLES.register("my_particle", () -> new ParticleType<&lt>;ParticleOptionsTwoInt>;(false) {
 *   public MapCodec<ParticleOptionsTwoInt>; codec() {
 *     return ParticleOptionsTwoInt.codec(this);
 *   }
 *  public StreamCodec<? super RegistryFriendlyByteBuf, ParticleOptionsTwoInt>; streamCodec() {
 *     return ParticleOptionsTwoInt.streamCodec(this);
 *   }
 * });
 * </pre>
 */
public class ParticleOptionsTwoInt implements ParticleOptions {

  private final ParticleType<ParticleOptionsTwoInt> particleType;
  public int oneInt, twoInt;

  public ParticleOptionsTwoInt(ParticleType<ParticleOptionsTwoInt> particleType, int oneInt, int twoInt) {
    this.particleType = particleType;
    this.oneInt = oneInt;
    this.twoInt = twoInt;
  }

  @Override
  public ParticleType<?> getType() {
    return this.particleType;
  }

  /**
   * Returns a MapCodec for command/config serialization.
   * Call this from your ParticleType's codec() override, passing {@code this}.
   */
  public static MapCodec<ParticleOptionsTwoInt> codec(ParticleType<ParticleOptionsTwoInt> type) {
    return RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.INT.fieldOf("oneInt").forGetter(p -> p.oneInt),
            Codec.INT.fieldOf("twoInt").forGetter(p -> p.twoInt)
        ).apply(instance, (a, b) -> new ParticleOptionsTwoInt(type, a, b))
    );
  }

  /**
   * Returns a StreamCodec for network serialization.
   * Call this from your ParticleType's streamCodec() override, passing {@code this}.
   */
  public static StreamCodec<RegistryFriendlyByteBuf, ParticleOptionsTwoInt> streamCodec(ParticleType<ParticleOptionsTwoInt> type) {
    return StreamCodec.composite(
        ByteBufCodecs.INT, p -> p.oneInt,
        ByteBufCodecs.INT, p -> p.twoInt,
        (a, b) -> new ParticleOptionsTwoInt(type, a, b)
    );
  }

  @Override
  public String toString() {
    return String.format("%s %d %d", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), oneInt, twoInt);
  }
}
