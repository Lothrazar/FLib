package com.lothrazar.library.util;

import com.lothrazar.library.packet.PacketFlib;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
//import net.minecraftforge.network.NetworkDirection;
//import net.minecraftforge.network.simple.SimpleChannel;
public class PacketUtil {

  @Deprecated
  public static void sendToAllClients(Object instance, Level world, PacketFlib packet) {
    if (world.isClientSide) {
      return;
    }
    System.out.println("TODO Deprecate PacketUtil for new codec handlers ");
    for (Player player : world.players()) {
      if (player instanceof ServerPlayer sp) {
//        instance.sendTo(packet, sp.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
      }
    }
  }
}
