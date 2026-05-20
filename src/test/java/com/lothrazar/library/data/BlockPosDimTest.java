package com.lothrazar.library.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

class BlockPosDimTest {

  private static final double EPS = 1e-9;

  @Test
  void toBlockPosReturnsStoredCoordinates() {
    BlockPosDim d = new BlockPosDim(new BlockPos(1, 2, 3), "minecraft:overworld");
    assertEquals(new BlockPos(1, 2, 3), d.toBlockPos());
  }

  @Test
  void getDistanceIsEuclidean() {
    BlockPosDim d = new BlockPosDim(new BlockPos(0, 0, 0), "minecraft:overworld");
    assertEquals(0.0, d.getDistance(new BlockPos(0, 0, 0)), EPS);
    assertEquals(5.0, d.getDistance(new BlockPos(3, 4, 0)), EPS);
  }

  @Test
  void displayStringStripsMinecraftPrefix() {
    BlockPosDim d = new BlockPosDim(new BlockPos(1, 2, 3), "minecraft:overworld");
    assertEquals("overworld (1, 2, 3)", d.getDisplayString());
  }

  @Test
  void equalsAndHashCodeMatchForSameValues() {
    BlockPosDim a = new BlockPosDim(new BlockPos(7, 8, 9), "minecraft:nether");
    BlockPosDim b = new BlockPosDim(new BlockPos(7, 8, 9), "minecraft:nether");
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  void differentDimensionIsNotEqual() {
    BlockPosDim a = new BlockPosDim(new BlockPos(7, 8, 9), "minecraft:nether");
    BlockPosDim b = new BlockPosDim(new BlockPos(7, 8, 9), "minecraft:overworld");
    assertNotEquals(a, b);
  }

  @Test
  void originIsRecognised() {
    assertTrue(new BlockPosDim(BlockPos.ZERO, "").isOrigin());
    assertFalse(new BlockPosDim(new BlockPos(1, 0, 0), "").isOrigin());
  }

  @Test
  void sideDefaultsToUpWhenUnset() {
    BlockPosDim d = new BlockPosDim(BlockPos.ZERO, "");
    assertEquals(Direction.UP, d.getSide());
    d.setSide(Direction.NORTH);
    assertEquals(Direction.NORTH, d.getSide());
  }
}
