package com.lothrazar.library.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TickComparableTest {

  @Test
  void compareToOtherUsesTickDifference() {
    TickComparable a = new TickComparable(10);
    TickComparable b = new TickComparable(4);
    assertEquals(6, a.compareTo(b));
    assertEquals(-6, b.compareTo(a));
    assertEquals(0, a.compareTo(new TickComparable(10)));
  }

  @Test
  void compareToIntOverload() {
    TickComparable a = new TickComparable(7);
    assertEquals(2, a.compareTo(5));
    assertEquals(0, a.compareTo(7));
  }

  @Test
  void tickDecrementsAndExpires() {
    TickComparable a = new TickComparable(2);
    assertFalse(a.isExpired());
    a.tick();
    assertEquals(1, a.getTick());
    assertFalse(a.isExpired());
    a.tick();
    assertEquals(0, a.getTick());
    assertTrue(a.isExpired());
  }

  @Test
  void expiredWhenZeroOrNegative() {
    assertTrue(new TickComparable(0).isExpired());
    assertTrue(new TickComparable(-3).isExpired());
    assertFalse(new TickComparable(1).isExpired());
  }

  @Test
  void decreaseSubtractsOtherTick() {
    TickComparable a = new TickComparable(10);
    a.decrease(new TickComparable(3));
    assertEquals(7, a.getTick());
  }

  @Test
  void setTickReplacesValue() {
    TickComparable a = new TickComparable();
    a.setTick(42);
    assertEquals(42, a.getTick());
  }
}
