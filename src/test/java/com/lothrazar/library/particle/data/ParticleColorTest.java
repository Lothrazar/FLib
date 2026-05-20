package com.lothrazar.library.particle.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ParticleColorTest {

  private static final float EPS = 1e-4f;

  @Test
  void constructorPacksRgbIntoColorInt() {
    ParticleColor c = new ParticleColor(255, 128, 64);
    assertEquals((255 << 16) | (128 << 8) | 64, c.getColor());
  }

  @Test
  void floatComponentsAreNormalized() {
    ParticleColor c = new ParticleColor(255, 0, 0);
    assertEquals(1.0f, c.getRed(), EPS);
    assertEquals(0.0f, c.getGreen(), EPS);
    assertEquals(0.0f, c.getBlue(), EPS);
  }

  @Test
  void fromIntUnpacksChannels() {
    ParticleColor c = ParticleColor.fromInt(0xFF8040);
    assertEquals(0xFF8040, c.getColor());
    assertEquals(1.0f, c.getRed(), EPS);
  }

  @Test
  void fromIntRoundTripsThroughGetColor() {
    int packed = (10 << 16) | (20 << 8) | 30;
    assertEquals(packed, ParticleColor.fromInt(packed).getColor());
  }

  @Test
  void deserializeParsesCommaSeparatedInts() {
    ParticleColor c = ParticleColor.deserialize("10, 20, 30");
    assertEquals((10 << 16) | (20 << 8) | 30, c.getColor());
  }

  @Test
  void intWrapperRoundTripsThroughString() {
    ParticleColor.IntWrapper w = new ParticleColor.IntWrapper(12, 34, 56);
    ParticleColor.IntWrapper back = ParticleColor.IntWrapper.deserialize(w.serialize());
    assertEquals(12, back.r);
    assertEquals(34, back.g);
    assertEquals(56, back.b);
  }

  @Test
  void intWrapperDeserializeFallsBackOnBadInput() {
    ParticleColor.IntWrapper w = ParticleColor.IntWrapper.deserialize("not-a-color");
    assertEquals(255, w.r);
    assertEquals(25, w.g);
    assertEquals(180, w.b);
  }

  @Test
  void makeVisibleBoostsDarkColors() {
    ParticleColor.IntWrapper dark = new ParticleColor.IntWrapper(1, 2, 3);
    dark.makeVisible();
    assertEquals(11, dark.r);
    assertEquals(12, dark.g);
    assertEquals(13, dark.b);
  }

  @Test
  void makeVisibleLeavesBrightColorsAlone() {
    ParticleColor.IntWrapper bright = new ParticleColor.IntWrapper(100, 100, 100);
    bright.makeVisible();
    assertEquals(100, bright.r);
    assertEquals(100, bright.g);
    assertEquals(100, bright.b);
  }
}
