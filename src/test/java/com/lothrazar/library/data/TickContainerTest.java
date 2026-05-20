package com.lothrazar.library.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TickContainerTest {

  @Test
  void storesTickAndData() {
    TickContainer<String> tc = new TickContainer<>(5, "payload");
    assertEquals(5, tc.getTick());
    assertEquals("payload", tc.getData());
  }

  @Test
  void setDataReplacesPayload() {
    TickContainer<String> tc = new TickContainer<>(5, "old");
    tc.setData("new");
    assertEquals("new", tc.getData());
  }

  @Test
  void inheritsTickBehaviour() {
    TickContainer<Integer> tc = new TickContainer<>(1, 99);
    tc.tick();
    assertTrue(tc.isExpired());
    assertEquals(99, tc.getData());
  }
}
