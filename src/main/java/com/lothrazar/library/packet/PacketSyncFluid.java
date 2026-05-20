package com.lothrazar.library.packet;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.core.IHasFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent server→client to sync fluid content on a block entity.
 */
public class PacketSyncFluid extends PacketFlib implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketSyncFluid> TYPE =
      new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "sync_fluid"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncFluid> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, PacketSyncFluid::getPos,
          FluidStack.OPTIONAL_STREAM_CODEC, PacketSyncFluid::getFluid,
          PacketSyncFluid::new);

  private final BlockPos pos;
  private final FluidStack fluid;

  public PacketSyncFluid(BlockPos pos, FluidStack fluid) {
    this.pos = pos;
    this.fluid = fluid;
  }

  public BlockPos getPos() { return pos; }
  public FluidStack getFluid() { return fluid; }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketSyncFluid msg, IPayloadContext context) {
    context.enqueueWork(() -> {
      BlockEntity te = Minecraft.getInstance().level.getBlockEntity(msg.pos);
      if (te instanceof IHasFluid tile) {
        tile.setFluid(msg.fluid);
      }
    });
  }
}
