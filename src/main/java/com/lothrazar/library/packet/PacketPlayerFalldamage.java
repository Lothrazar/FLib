package com.lothrazar.library.packet;


import com.lothrazar.library.FutureLibMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Used by: Fan block; Launch enchant; Air charm; Climbing Glove; Scaffolding Block
 */
public class PacketPlayerFalldamage implements PacketFlib {

  public static final int TICKS_FALLDIST_SYNC = 22; //tick every so often

  public static final CustomPacketPayload.Type<PacketPlayerFalldamage> TYPE =
          new CustomPacketPayload.Type<>(FutureLibMod.rl( "fall_damage"));

  public static final PacketPlayerFalldamage INSTANCE = new PacketPlayerFalldamage();
  public static final StreamCodec<ByteBuf, PacketPlayerFalldamage> STREAM_CODEC = StreamCodec.unit(INSTANCE);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  @Override
  public void handle(IPayloadContext context) {
      Player player = context.player();
      /**
       * if fall damage gets high, they take damage on landing
       */
      player.fallDistance = 0.0F;
      /**
       * Used to keep track of how the player is floating while gamerules should prevent that. Surpassing 80 ticks means kick
       */
    if(context.player() instanceof ServerPlayer sp) {
      // sp.connection.aboveGroundTickCount = 0; // set to public in accesstransformer
      // TODO: 1.21 aboveGroundTickCount migration
    }
  }
}
