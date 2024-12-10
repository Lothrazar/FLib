package com.lothrazar.library.mod;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.packet.PacketPlayerFalldamage;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

public class PacketRegistry {


//  private static final String PROTOCOL_VERSION = Integer.toString(1);
//
//  @SubscribeEvent
//  public static void register(final RegisterPayloadHandlerEvent event) {
//    final IPayloadRegistrar registrar = event.registrar("my_mod")
//            .versioned("1.2.3")
//            .optional();
//  }
public static final PacketRegistry INSTANCE = new PacketRegistry();


  public void sendToServer(CustomPacketPayload message) {
    PacketDistributor.sendToServer(message, new CustomPacketPayload[0]);
  }


  public void sendTo(CustomPacketPayload message, ServerPlayer player) {
    player.connection.send(message);
  }


//  public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
//      .named(ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "main_channel"))
//      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
//      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
//      .networkProtocolVersion(() -> PROTOCOL_VERSION)
//      .simpleChannel();

  public static void setup() {
//    int id = 0;
//    INSTANCE.registerMessage(id++, PacketPlayerFalldamage.class, PacketPlayerFalldamage::encode, PacketPlayerFalldamage::decode, PacketPlayerFalldamage::handle);
  }
}
