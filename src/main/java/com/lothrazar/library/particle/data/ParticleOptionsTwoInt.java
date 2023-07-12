package com.lothrazar.library.particle.data;

import java.util.Locale;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * used by ParticleBlinkingAura
 *
 */
public class ParticleOptionsTwoInt implements ParticleOptions {

  @SuppressWarnings("deprecation")
  public static final Deserializer<ParticleOptionsTwoInt> DESERIALIZER = new Deserializer<ParticleOptionsTwoInt>() {

    @Override
    public ParticleOptionsTwoInt fromCommand(ParticleType<ParticleOptionsTwoInt> particleType, StringReader reader) throws CommandSyntaxException {
      if (reader.canRead()) {
        reader.expect(' ');
      }
      int oneInt = 0xffffff, twoInt = 0xffffff;
      if (reader.canRead()) {
        oneInt = reader.readInt();
      }
      if (reader.canRead()) {
        reader.expect(' ');
      }
      if (reader.canRead()) {
        twoInt = reader.readInt();
      }
      return new ParticleOptionsTwoInt(particleType, oneInt, twoInt);
    }

    @Override
    public ParticleOptionsTwoInt fromNetwork(ParticleType<ParticleOptionsTwoInt> particleType, FriendlyByteBuf buf) {
      return new ParticleOptionsTwoInt(particleType, buf.readInt(), buf.readInt());
    }
  };
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

  @Override
  public void writeToNetwork(FriendlyByteBuf buf) {
    buf.writeInt(this.oneInt);
    buf.writeInt(this.twoInt);
  }

  @Override
  public String writeToString() {
    return String.format(Locale.ROOT, "%s %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), this.oneInt, this.twoInt);
  }
}
