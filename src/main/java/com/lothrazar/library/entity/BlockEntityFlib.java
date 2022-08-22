package com.lothrazar.library.entity;

import java.lang.ref.WeakReference;
import com.lothrazar.library.block.BlockFlib;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.FakePlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;

public abstract class BlockEntityFlib extends BlockEntity {

  public BlockEntityFlib(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    //    timer = tag.getInt("timer");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    //    tag.putInt("timer", timer);
    super.saveAdditional(tag);
  }

  public abstract void setField(int field, int value);

  public abstract int getField(int field);

  public void setLitProperty(boolean lit) {
    BlockState st = this.getBlockState();
    if (st.hasProperty(BlockFlib.LIT)) {
      boolean previous = st.getValue(BlockFlib.LIT);
      if (previous != lit) {
        this.level.setBlockAndUpdate(worldPosition, st.setValue(BlockFlib.LIT, lit));
      }
    }
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag syncData = super.getUpdateTag();
    this.saveAdditional(syncData);
    return syncData;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    this.load(pkt.getTag());
    super.onDataPacket(net, pkt);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public boolean isPowered() {
    return this.getLevel().hasNeighborSignal(this.getBlockPos());
  }

  public int getRedstonePower() {
    return this.getLevel().getBestNeighborSignal(this.getBlockPos());
  }

  public WeakReference<FakePlayer> buildFakePlayer(ServerLevel sw, final String name, final Direction facing) {
    WeakReference<FakePlayer> fakePlayer = FakePlayerUtil.initFakePlayer(sw, name);
    if (fakePlayer == null) {
      //      ModCyclic.LOGGER.error("Fake player failed to init " + name);
      return null;
    }
    //fake player facing the same direction as tile. for throwables
    fakePlayer.get().setPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()); //seems to help interact() mob drops like milk
    fakePlayer.get().setYRot(EntityUtil.getYawFromFacing(facing));
    return fakePlayer;
  }

  public static InteractionResult playerAttackBreakBlock(WeakReference<FakePlayer> fakePlayer, Level world, BlockPos targetPos, InteractionHand hand, Direction facing) {
    if (fakePlayer == null) {
      return InteractionResult.FAIL;
    }
    try {
      fakePlayer.get().gameMode.handleBlockBreakAction(targetPos, ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, facing, world.getMaxBuildHeight());
      return InteractionResult.SUCCESS;
    }
    catch (Exception e) {
      return InteractionResult.FAIL;
    }
  }

  public static boolean tryHarvestBlock(WeakReference<FakePlayer> fakePlayer, Level world, BlockPos targetPos) {
    if (fakePlayer == null) {
      return false;
    }
    return fakePlayer.get().gameMode.destroyBlock(targetPos);
  }
}
