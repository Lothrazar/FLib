package com.lothrazar.library.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter; 

public class BlockLayeringLeaves extends BlockLayering {

  public BlockLayeringLeaves(Block main, Properties props) {
    super(main, props);
  }

  @Override
  public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 255;
  }
}
