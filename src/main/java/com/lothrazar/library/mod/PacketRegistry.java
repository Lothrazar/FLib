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
  public static void register(final net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
    final net.neoforged.neoforge.network.registration.PayloadRegistrar registrar = event.registrar(FutureLibMod.MODID)
            .versioned("1.0.0");
    registrar.playToServer(PacketPlayerFalldamage.TYPE, PacketPlayerFalldamage.STREAM_CODEC, PacketPlayerFalldamage::handle);
    registrar.playToServer(com.lothrazar.library.packet.PacketItemToggle.TYPE, com.lothrazar.library.packet.PacketItemToggle.STREAM_CODEC, com.lothrazar.library.packet.PacketItemToggle::handle);
    registrar.playToServer(com.lothrazar.library.packet.PacketRotateBlock.TYPE, com.lothrazar.library.packet.PacketRotateBlock.STREAM_CODEC, com.lothrazar.library.packet.PacketRotateBlock::handle);
    registrar.playToClient(com.lothrazar.library.packet.PacketSyncEnergy.TYPE, com.lothrazar.library.packet.PacketSyncEnergy.STREAM_CODEC, com.lothrazar.library.packet.PacketSyncEnergy::handle);
    registrar.playToClient(com.lothrazar.library.packet.PacketSyncFluid.TYPE, com.lothrazar.library.packet.PacketSyncFluid.STREAM_CODEC, com.lothrazar.library.packet.PacketSyncFluid::handle);
  }
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
