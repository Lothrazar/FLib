package com.lothrazar.library.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.minecraft.core.BlockPos;

class BlockPosUtilTest {

  @Test
  void sortByDistanceOrdersClosestFirst() {
    BlockPos origin = new BlockPos(0, 0, 0);
    List<BlockPos> positions = new ArrayList<>();
    BlockPos far = new BlockPos(10, 0, 0);
    BlockPos near = new BlockPos(1, 0, 0);
    BlockPos mid = new BlockPos(5, 0, 0);
    positions.add(far);
    positions.add(near);
    positions.add(mid);
    BlockPosUtil.sortByDistance(origin, positions);
    assertEquals(near, positions.get(0));
    assertEquals(mid, positions.get(1));
    assertEquals(far, positions.get(2));
  }

  @Test
  void sortByDistanceHandlesEmptyList() {
    List<BlockPos> positions = new ArrayList<>();
    BlockPosUtil.sortByDistance(BlockPos.ZERO, positions);
    assertEquals(0, positions.size());
  }
}
