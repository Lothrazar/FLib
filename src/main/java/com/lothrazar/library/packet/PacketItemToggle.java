package com.lothrazar.library.packet;

import java.util.function.Supplier;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.core.IHasClickToggle;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketItemToggle(int slot) implements PacketFlib {

  public static final CustomPacketPayload.Type<PacketItemToggle> TYPE =
          new CustomPacketPayload.Type<>(FutureLibMod.rl( "item_toggle"));
  public static final StreamCodec<ByteBuf, PacketItemToggle> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.INT, PacketItemToggle::slot,
          PacketItemToggle::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  @Override
  public void handle(IPayloadContext context) {

    Player player = context.player();
    if (player.containerMenu == null) {
      return;
    }
    int scount = player.containerMenu.slots.size();
    //this is an edge case but it DID happen: put charmin your hotbar and then open a creative inventory tab. avoid index OOB
    if (this.slot >= scount) {
      //will NOT work in creative mode. slots are messed up
      return;
    }
    Slot slotObj = player.containerMenu.getSlot(this.slot());
    if (slotObj != null && !slotObj.getItem().isEmpty()) {
      ItemStack maybeCharm = slotObj.getItem();
      if (maybeCharm.getItem() instanceof IHasClickToggle) {
        //example: is a charm or something
        IHasClickToggle c = (IHasClickToggle) maybeCharm.getItem();
        c.toggle(player, maybeCharm);
      }
    }
  }
}
