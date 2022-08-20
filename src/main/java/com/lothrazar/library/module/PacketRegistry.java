package com.lothrazar.library.module;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.packet.PacketPlayerFalldamage;
import com.lothrazar.library.packet.PacketSyncManaToClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketRegistry {

  private static final String PROTOCOL_VERSION = Integer.toString(1);
  public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(FutureLibMod.MODID, "main_channel"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();

  public static void setup() {
    int id = 0;
    INSTANCE.registerMessage(id++, PacketPlayerFalldamage.class, PacketPlayerFalldamage::encode, PacketPlayerFalldamage::decode, PacketPlayerFalldamage::handle);
    INSTANCE.registerMessage(id++, PacketSyncManaToClient.class, PacketSyncManaToClient::encode, PacketSyncManaToClient::decode, PacketSyncManaToClient::handle);
  }
  //  public static void sendToAllClients(Level world, PacketBaseCyclic packet) {
  //    if (world.isClientSide) {
  //      return;
  //    }
  //    for (Player player : world.players()) {
  //      ServerPlayer sp = ((ServerPlayer) player);
  //      PacketRegistry.INSTANCE.sendTo(packet, sp.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
  //    }
  //  }
}
