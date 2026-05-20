package com.lothrazar.library.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import org.junit.jupiter.api.Test;

class Vector3Test {

  private static final double EPS = 1e-9;

  @Test
  void magnitudeOf3_4_0Is5() {
    assertEquals(5.0, new Vector3(3, 4, 0).mag(), EPS);
  }

  @Test
  void normalizeProducesUnitLength() {
    Vector3 v = new Vector3(3, 4, 0).normalize();
    assertEquals(1.0, v.mag(), EPS);
    assertEquals(0.6, v.x, EPS);
    assertEquals(0.8, v.y, EPS);
  }

  @Test
  void normalizeZeroVectorIsSafe() {
    Vector3 v = new Vector3(0, 0, 0).normalize();
    assertEquals(0.0, v.mag(), EPS);
  }

  @Test
  void multiplyScalesEachComponent() {
    Vector3 v = new Vector3(1, 2, 3).multiply(2);
    assertEquals(2.0, v.x, EPS);
    assertEquals(4.0, v.y, EPS);
    assertEquals(6.0, v.z, EPS);
  }

  @Test
  void subtractIsComponentwise() {
    Vector3 v = new Vector3(5, 5, 5).subtract(new Vector3(1, 2, 3));
    assertEquals(4.0, v.x, EPS);
    assertEquals(3.0, v.y, EPS);
    assertEquals(2.0, v.z, EPS);
  }

  @Test
  void copyIsIndependentInstance() {
    Vector3 original = new Vector3(1, 2, 3);
    Vector3 clone = original.copy();
    assertNotSame(original, clone);
    clone.multiply(10);
    assertEquals(1.0, original.x, EPS);
    assertEquals(10.0, clone.x, EPS);
  }
}
