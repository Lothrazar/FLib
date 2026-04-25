package com.lothrazar.library.util;

import com.lothrazar.library.packet.PacketFlib;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class PacketUtil {

  public static void sendToAllClients(Level world, PacketFlib packet) {
    if (world.isClientSide || !(packet instanceof CustomPacketPayload payload)) {
      return;
    }
    for (Player player : world.players()) {
      if (player instanceof ServerPlayer sp) {
        PacketDistributor.sendToPlayer(sp, payload);
      }
    }
  }

  /**
   * Send a packet to a specific player.
   */
  public static void sendToPlayer(ServerPlayer player, CustomPacketPayload packet) {
    PacketDistributor.sendToPlayer(player, packet);
  }
}
