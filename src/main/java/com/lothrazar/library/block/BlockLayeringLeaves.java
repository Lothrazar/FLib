package com.lothrazar.library.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockLayeringLeaves extends BlockLayering {

  public BlockLayeringLeaves(Block main, Properties props) {
    super(main, props);
  }

  @Override
  protected int getLightDampening(BlockState state) {
    return 15;
  }
}
