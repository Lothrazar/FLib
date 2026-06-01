package com.lothrazar.library.packet;

import com.lothrazar.library.FutureLibMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent client→server to reset fall distance (used by climbing, fan blocks, etc.).
 */
public class PacketPlayerFalldamage extends PacketFlib implements CustomPacketPayload {

  public static final int TICKS_FALLDIST_SYNC = 22;

  public static final CustomPacketPayload.Type<PacketPlayerFalldamage> TYPE =
      new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "fall_damage"));

  //  public static final StreamCodec<FriendlyByteBuf, PacketPlayerFalldamage> STREAM_CODEC =
  //      StreamCodec.unit(new PacketPlayerFalldamage());
  // StreamCodec.unit(value) uses reference equality and rejects any other instance,
  // so any caller doing `new PacketPlayerFalldamage()` blows up at encode time.
  // This empty-payload codec accepts any instance and round-trips zero bytes.
  public static final StreamCodec<FriendlyByteBuf, PacketPlayerFalldamage> STREAM_CODEC =
        StreamCodec.of((buf, pkt) -> {}, buf -> new PacketPlayerFalldamage());

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketPlayerFalldamage msg, IPayloadContext context) {
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      player.fallDistance = 0.0F;
      player.connection.aboveGroundTickCount = 0;
    });
  }
}
