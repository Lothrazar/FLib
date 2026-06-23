package com.lothrazar.library.mod;

import com.lothrazar.library.packet.*;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketRegistry {

  public static void setup(FMLCommonSetupEvent event) {
    // Registration now happens via RegisterPayloadHandlersEvent on the mod bus.
    // This method is kept for API compatibility but does nothing.
  }

  public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar("1");
    registrar.playToServer(PacketPlayerFalldamage.TYPE, PacketPlayerFalldamage.STREAM_CODEC, PacketPlayerFalldamage::handle);
    registrar.playToServer(PacketItemToggle.TYPE, PacketItemToggle.STREAM_CODEC, PacketItemToggle::handle);
    registrar.playToServer(PacketRotateBlock.TYPE, PacketRotateBlock.STREAM_CODEC, PacketRotateBlock::handle);
    registrar.playToClient(PacketSyncEnergy.TYPE, PacketSyncEnergy.STREAM_CODEC, PacketSyncEnergy::handle);
    registrar.playToClient(PacketSyncFluid.TYPE, PacketSyncFluid.STREAM_CODEC, PacketSyncFluid::handle);
    registrar.playToServer(BlockFacadeMessage.TYPE, BlockFacadeMessage.STREAM_CODEC, BlockFacadeMessage::handle);
  }
}
