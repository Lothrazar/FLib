package com.lothrazar.library.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author https://github.com/RedRelay/ForgeCreeperHeal https://github.com/Lothrazar/CreeperHeal
 *
 * 
 */
public class BlockStatePosWrapper {

  private BlockPos blockPos;
  private BlockState blockState;
  private CompoundTag tileEntityTag;

  public BlockStatePosWrapper(Level world, BlockPos chunkPosition, BlockState blockState) {
    this.blockState = blockState;
    this.blockPos = chunkPosition;
    BlockEntity te = world.getBlockEntity(chunkPosition);
    if (te != null) {
      this.tileEntityTag = te.saveWithoutMetadata();
    }
  }

  public BlockStatePosWrapper() {}

  public BlockState getBlockState() {
    return blockState;
  }

  public BlockPos getBlockPos() {
    return blockPos;
  }

  public CompoundTag getTileEntityTag() {
    return tileEntityTag;
  }

  public void readFromNBT(CompoundTag tag, Level level) {
    this.blockState = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), tag.getCompound("block"));
    this.blockPos = NbtUtils.readBlockPos(tag.getCompound("pos"));
    if (tag.contains("tileentity")) {
      this.tileEntityTag = tag.getCompound("tileentity");
    }
  }

  public void writeToNBT(CompoundTag tag) {
    CompoundTag encoded = NbtUtils.writeBlockState(this.blockState);
    tag.put("block", encoded);
    CompoundTag epos = NbtUtils.writeBlockPos(this.blockPos);
    tag.put("pos", epos);
    if (this.tileEntityTag != null) {
      tag.put("tileentity", this.tileEntityTag);
    }
  }

  public void setBlockState(BlockState blockState) {
    this.blockState = blockState;
  }

  public void setTileEntityTag(CompoundTag tileEntityTag) {
    this.tileEntityTag = tileEntityTag;
  }

  public void setBlockPos(BlockPos blockPos) {
    this.blockPos = blockPos;
  }
}
