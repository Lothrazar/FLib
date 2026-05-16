package com.lothrazar.library.data;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.library.mod.DataComponentsFlib;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RelativeShape {

  public static final String VALID_SHAPE = "cyclic-shape";
  private String structure = null;
  //corner small always has the lowest Y point, 
  //if same y level, whoever has smallest sum of x+z, closest to negative infinity
  //should not allow points outside corners
  private List<BlockPos> shape;
  private int count;

  public RelativeShape(RelativeShape other) {
    this.shape = other.shape;
    this.count = other.count;
  }

  public RelativeShape() {
    shape = new ArrayList<>();
  }
  public static final Codec<RelativeShape> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.optionalFieldOf("structure", "").forGetter(s -> s.structure != null ? s.structure : ""),
      BlockPos.CODEC.listOf().fieldOf("shape")
          .forGetter(RelativeShape::getShape))
      .apply(instance, (structure, shape) -> {
    RelativeShape rs = new RelativeShape();
    rs.setStructure(structure.isEmpty() ? null : structure);
    rs.setShape(shape);
    return rs;
  }));

  public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, RelativeShape> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
      net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8, s -> s.structure != null ? s.structure : "",
      BlockPos.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), RelativeShape::getShape,
      (structure, shape) -> {
        RelativeShape rs = new RelativeShape();
        rs.setStructure(structure.isEmpty() ? null : structure);
        rs.setShape(shape);
        return rs;
      }
  );
  public void merge(RelativeShape other) {
    shape.addAll(other.shape);
    count = shape.size();
  }

  /**
   * if world is null, it will not check for air blocks. if world is provided. will delete any spots of air. Does change all positions to offset from center
   *
   * @param world
   * @param options
   * @param center
   */
  public RelativeShape(Level world, List<BlockPos> options, BlockPos center) {
    this();
    this.shape = options;
    if (world != null && center != null) {
      setWorldCenter(world, center);
    }
  }

  public void setWorldCenter(Level world, BlockPos center) {
    if (world != null) {
      List<BlockPos> options = shape;
      shape = new ArrayList<>();
      for (BlockPos pos : options) {
        BlockState bs = world.getBlockState(pos);
        if (!bs.isAir()) {
          shape.add(pos.offset(-1 * center.getX(), -1 * center.getY(), -1 * center.getZ()));
        }
      }
    }
    count = shape.size();
  }

  public int getCount() {
    return count;
  }

  public String getStructure() {
    return structure;
  }

  public void setStructure(String structure) {
    this.structure = structure;
  }

  public List<BlockPos> getShape() {
    return shape;
  }

  public static RelativeShape read(CompoundTag tag) {
    if (tag == null || tag.getBoolean(RelativeShape.VALID_SHAPE) == false) {
      return null;
    }
    int count = tag.getInt("count");
    List<BlockPos> shapeList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      shapeList.add(new BlockPos(tag.getInt("x" + i), tag.getInt("y" + i), tag.getInt("z" + i)));
    }
    RelativeShape shape = new RelativeShape();
    shape.shape = shapeList;
    shape.count = shapeList.size();
    return shape;
  }

  public static RelativeShape read(ItemStack item) {
    return item.get(DataComponentsFlib.RELATIVE_SHAPE.get());
  }

  public CompoundTag write(CompoundTag tag) {
    int i = 0;
    int count = 0;
    for (BlockPos p : shape) {
      tag.putInt("x" + i, p.getX());
      tag.putInt("y" + i, p.getY());
      tag.putInt("z" + i, p.getZ());
      i++;
      count = i;
    }
    tag.putInt("count", count);
    tag.putBoolean(RelativeShape.VALID_SHAPE, true);
    return tag;
  }

  public void write(ItemStack shapeCard) {
    shapeCard.set(DataComponentsFlib.RELATIVE_SHAPE.get(), this);
  }

  public void setShape(List<BlockPos> list) {
    this.shape = list;
    this.count = this.shape.size();
  }
}
