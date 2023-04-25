package com.lothrazar.library.mod;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.packet.PacketPlayerFalldamage;
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
  }
}
