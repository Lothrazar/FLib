package com.lothrazar.library.packet;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.core.IHasEnergy;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent server→client to sync energy level on a block entity.
 */
public class PacketSyncEnergy extends PacketFlib implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketSyncEnergy> TYPE =
      new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "sync_energy"));

  private static final StreamCodec<FriendlyByteBuf, PacketSyncEnergy> INNER_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, PacketSyncEnergy::getPos,
          ByteBufCodecs.INT, PacketSyncEnergy::getEnergy,
          PacketSyncEnergy::new);

  public static final StreamCodec<FriendlyByteBuf, PacketSyncEnergy> STREAM_CODEC = StreamCodec.of(
      (buf, msg) -> {
//        int before = buf.writerIndex();
        INNER_CODEC.encode(buf, msg);
       // FutureLibMod.LOGGER.debug("[PKT-DBG] encode PacketSyncEnergy wrote={}", buf.writerIndex() - before);
      },
      buf -> {
//        int before = buf.readableBytes();
        PacketSyncEnergy p = INNER_CODEC.decode(buf);
//        FutureLibMod.LOGGER.debug("[PKT-DBG] decode PacketSyncEnergy before={} consumed={} remaining={}",
//            before, before - buf.readableBytes(), buf.readableBytes());
        return p;
      });

  private final BlockPos pos;
  private final int energy;

  public PacketSyncEnergy(BlockPos pos, int energy) {
    this.pos = pos;
    this.energy = energy;
  }

  public BlockPos getPos() { return pos; }
  public int getEnergy() { return energy; }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketSyncEnergy msg, IPayloadContext context) {
    context.enqueueWork(() -> {
      BlockEntity te = Minecraft.getInstance().level.getBlockEntity(msg.pos);
      if (te instanceof IHasEnergy tile) {
        tile.setEnergy(msg.energy);
      }
    });
  }
}
