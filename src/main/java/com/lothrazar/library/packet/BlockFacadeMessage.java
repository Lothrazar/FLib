package com.lothrazar.library.packet;

import com.lothrazar.library.core.IBlockFacade;
import com.lothrazar.library.core.ITileFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BlockFacadeMessage extends PacketFlib implements CustomPacketPayload {

  public static final Type<BlockFacadeMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath("flib", "block_facade_message"));

  public static final StreamCodec<RegistryFriendlyByteBuf, BlockFacadeMessage> STREAM_CODEC = StreamCodec.of(BlockFacadeMessage::encode, BlockFacadeMessage::decode);


  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  private BlockPos pos;
  private boolean erase = false;
  private CompoundTag blockStateTag = new CompoundTag();

  private BlockFacadeMessage() {}

  public BlockFacadeMessage(BlockPos pos, CompoundTag state) {
    this.pos = pos;
    this.blockStateTag = state;
    this.erase = false;
  }

  public BlockFacadeMessage(BlockPos pos, boolean eraseIn) {
    this.pos = pos;
    this.erase = eraseIn;
    blockStateTag = new CompoundTag();
  }

  public static void handle(BlockFacadeMessage message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) ctx.player();
      ServerLevel serverWorld = (ServerLevel) player.level();
      BlockState bs = serverWorld.getBlockState(message.pos);
      if (bs.getBlock() instanceof IBlockFacade facadeBlock) {
        //
        ITileFacade tile = facadeBlock.getTileFacade(serverWorld, message.pos);
        if (message.erase) {
          tile.setFacade(new CompoundTag());
        }
        else {
          tile.setFacade(message.blockStateTag);
        }
        if (bs.hasProperty(IBlockFacade.HAS_FACADE)) {
          boolean hasFacade = !message.erase && !message.blockStateTag.isEmpty();
          serverWorld.setBlockAndUpdate(message.pos, bs.setValue(IBlockFacade.HAS_FACADE, hasFacade));
        }
        serverWorld.markAndNotifyBlock(message.pos, serverWorld.getChunkAt(message.pos),
            bs, bs, 3, 1);
        serverWorld.sendBlockUpdated(message.pos, bs, bs, 3);
        // TODO 26.1 port: Level#blockUpdated was removed with no direct replacement found;
        // dropped since markAndNotifyBlock + sendBlockUpdated above already cover notification.
      }
    });
    // ctx.setPacketHandled(true);
  }

  public static BlockFacadeMessage decode(RegistryFriendlyByteBuf buf) {
    BlockFacadeMessage message = new BlockFacadeMessage();
    message.erase = buf.readBoolean();
    message.pos = buf.readBlockPos();
    message.blockStateTag = buf.readNbt();
    return message;
  }

  public static void encode(RegistryFriendlyByteBuf buf, BlockFacadeMessage msg) {
    buf.writeBoolean(msg.erase);
    buf.writeBlockPos(msg.pos);
    buf.writeNbt(msg.blockStateTag);
  }
}
