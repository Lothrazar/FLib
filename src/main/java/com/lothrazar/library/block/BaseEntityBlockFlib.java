package com.lothrazar.library.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseEntityBlockFlib extends BlockFlib implements EntityBlock {

  public BaseEntityBlockFlib(Properties prop, BlockFlib.Settings custom) {
    super(prop, custom);
  }

  /**
   * Has tooltip by default
   * 
   * @param prop
   */
  public BaseEntityBlockFlib(Properties prop) {
    this(prop, new BlockFlib.Settings().tooltip());
  }

  @Override
  public RenderShape getRenderShape(BlockState bs) {
    return RenderShape.MODEL;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean triggerEvent(BlockState bs, Level level, BlockPos pos, int a, int b) {
    super.triggerEvent(bs, level, pos, a, b);
    BlockEntity blockentity = level.getBlockEntity(pos);
    return blockentity == null ? false : blockentity.triggerEvent(a, b);
  }

  @Override
  @Nullable
  public MenuProvider getMenuProvider(BlockState bs, Level level, BlockPos pos) {
    BlockEntity blockentity = level.getBlockEntity(pos);
    return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> etype, BlockEntityTicker<? super E> ticker) {
    return etype == type ? (BlockEntityTicker<A>) ticker : null;
  }
}
