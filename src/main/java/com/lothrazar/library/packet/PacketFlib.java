package com.lothrazar.library.packet;

import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketFlib {

  public void done(Supplier<Context> ctx) {
    ctx.get().setPacketHandled(true);
  }
}
