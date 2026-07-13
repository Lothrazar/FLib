package com.lothrazar.library.packet;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.core.IHasClickToggle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketItemToggle extends PacketFlib implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketItemToggle> TYPE =
      new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "item_toggle"));

  public static final StreamCodec<FriendlyByteBuf, PacketItemToggle> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.INT, PacketItemToggle::getSlot,
          PacketItemToggle::new);

  private final int slot;

  public PacketItemToggle(int slot) {
    this.slot = slot;
  }

  public int getSlot() {
    return slot;
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketItemToggle msg, IPayloadContext context) {
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      if (player.containerMenu == null) return;
      int scount = player.containerMenu.slots.size();
      if (msg.slot >= scount) return;
      Slot slotObj = player.containerMenu.getSlot(msg.slot);
      if (slotObj != null && !slotObj.getItem().isEmpty()) {
        ItemStack maybeCharm = slotObj.getItem();
        if (maybeCharm.getItem() instanceof IHasClickToggle c) {
          c.toggle(player, maybeCharm);
        }
      }
    });
  }
}
