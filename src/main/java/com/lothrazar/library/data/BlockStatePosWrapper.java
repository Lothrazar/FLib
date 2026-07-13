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
      this.tileEntityTag = te.saveWithoutMetadata(world.registryAccess());
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
    this.blockState = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), tag.getCompoundOrEmpty("block"));
    this.blockPos = tag.read("pos", BlockPos.CODEC).orElse(null);
    if (tag.contains("tileentity")) {
      this.tileEntityTag = tag.getCompoundOrEmpty("tileentity");
    }
  }

  public void writeToNBT(CompoundTag tag) {
    CompoundTag encoded = NbtUtils.writeBlockState(this.blockState);
    tag.put("block", encoded);
    tag.store("pos", BlockPos.CODEC, this.blockPos);
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
