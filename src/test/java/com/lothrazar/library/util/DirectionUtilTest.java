package com.lothrazar.library.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.minecraft.core.Direction;

class DirectionUtilTest {

  @Test
  void allPermutationsOfSixDirectionsExist() {
    // 6! == 720 distinct orderings
    assertEquals(720, DirectionUtil.ALL_DIFFERENT_ORDER.size());
  }

  @Test
  void everyPermutationContainsAllSixDirectionsOnce() {
    for (List<Direction> order : DirectionUtil.ALL_DIFFERENT_ORDER) {
      assertEquals(6, order.size());
      assertEquals(6, new HashSet<>(order).size());
    }
  }

  @Test
  void getAllInDifferentOrderReturnsValidPermutation() {
    List<Direction> order = DirectionUtil.getAllInDifferentOrder();
    assertEquals(6, order.size());
    assertEquals(6, new HashSet<>(order).size());
  }

  @Test
  void getAllInDifferentOrderCyclesThroughTable() {
    List<Direction> first = DirectionUtil.getAllInDifferentOrder();
    List<Direction> second = DirectionUtil.getAllInDifferentOrder();
    assertEquals(first, getEntryAfter(second));
  }

  private static List<Direction> getEntryAfter(List<Direction> current) {
    int idx = DirectionUtil.ALL_DIFFERENT_ORDER.indexOf(current);
    int prev = (idx - 1 + DirectionUtil.ALL_DIFFERENT_ORDER.size()) % DirectionUtil.ALL_DIFFERENT_ORDER.size();
    return DirectionUtil.ALL_DIFFERENT_ORDER.get(prev);
  }
}
