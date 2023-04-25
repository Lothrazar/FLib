package com.lothrazar.library.util;

import com.lothrazar.library.packet.PacketFlib;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketUtil {

  public static void sendToAllClients(SimpleChannel instance, Level world, PacketFlib packet) {
    if (world.isClientSide) {
      return;
    }
    for (Player player : world.players()) {
      if (player instanceof ServerPlayer sp) {
        instance.sendTo(packet, sp.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
      }
    }
  }
}
