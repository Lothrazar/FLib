package com.lothrazar.library.fluid;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * For fluid that wants collissions and is not full height like farmland.
 *
 * Used by cyclic/magma and cyclic/slime
 */
public class PartialHeightFluidBlock extends GenericFluidBlock {

  private final VoxelShape[] shapes = new VoxelShape[16];

  public PartialHeightFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties props) {
    super(supplier, props);
    float offset = 0.875F;
    for (int i = 0; i <= 15; i++) {
      shapes[i] = Shapes.create(new AABB(0, 0, 0, 1, offset - i / 8F, 1));
    }
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    return shapes[state.getValue(LEVEL).intValue()];
  }

  @Override
  @Deprecated
  public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
    return shapes[state.getValue(LEVEL).intValue()];
  }
}
