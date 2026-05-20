package com.lothrazar.library.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

class RelativeShapeTest {

  @Test
  void setShapeUpdatesCount() {
    RelativeShape shape = new RelativeShape();
    assertEquals(0, shape.getCount());
    shape.setShape(List.of(new BlockPos(0, 0, 0), new BlockPos(1, 1, 1)));
    assertEquals(2, shape.getCount());
  }

  @Test
  void mergeAppendsAndRecountsShape() {
    RelativeShape a = new RelativeShape();
    a.setShape(new java.util.ArrayList<>(List.of(new BlockPos(0, 0, 0))));
    RelativeShape b = new RelativeShape();
    b.setShape(List.of(new BlockPos(1, 1, 1), new BlockPos(2, 2, 2)));
    a.merge(b);
    assertEquals(3, a.getCount());
    assertEquals(3, a.getShape().size());
  }

  @Test
  void compoundTagWriteThenReadRoundTrips() {
    RelativeShape original = new RelativeShape();
    original.setShape(List.of(new BlockPos(1, 2, 3), new BlockPos(-4, 5, -6)));
    CompoundTag tag = original.write(new CompoundTag());

    RelativeShape restored = RelativeShape.read(tag);
    assertEquals(2, restored.getCount());
    assertEquals(new BlockPos(1, 2, 3), restored.getShape().get(0));
    assertEquals(new BlockPos(-4, 5, -6), restored.getShape().get(1));
  }

  @Test
  void readReturnsNullForNullOrInvalidTag() {
    assertNull(RelativeShape.read((CompoundTag) null));
    assertNull(RelativeShape.read(new CompoundTag()));
  }

  @Test
  void structureFieldIsStored() {
    RelativeShape shape = new RelativeShape();
    shape.setStructure("my-structure");
    assertEquals("my-structure", shape.getStructure());
  }
}
