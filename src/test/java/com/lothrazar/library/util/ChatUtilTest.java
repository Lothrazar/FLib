package com.lothrazar.library.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import net.minecraft.core.BlockPos;

class ChatUtilTest {

  @Test
  void blockPosToStringFormatsCoordinates() {
    assertEquals("1, 2, 3", ChatUtil.blockPosToString(new BlockPos(1, 2, 3)));
  }

  @Test
  void blockPosToStringHandlesNegatives() {
    assertEquals("-4, 0, -16", ChatUtil.blockPosToString(new BlockPos(-4, 0, -16)));
  }
}
