package com.lothrazar.library.packet;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.util.BlockUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketRotateBlock extends PacketFlib implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketRotateBlock> TYPE =
      new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "rotate_block"));

  private static final StreamCodec<FriendlyByteBuf, Direction> DIRECTION_CODEC =
      StreamCodec.of((buf, dir) -> buf.writeEnum(dir), buf -> buf.readEnum(Direction.class));

  private static final StreamCodec<FriendlyByteBuf, InteractionHand> HAND_CODEC =
      StreamCodec.of((buf, hand) -> buf.writeEnum(hand), buf -> buf.readEnum(InteractionHand.class));

  public static final StreamCodec<FriendlyByteBuf, PacketRotateBlock> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, PacketRotateBlock::getPos,
          DIRECTION_CODEC, PacketRotateBlock::getSide,
          HAND_CODEC, PacketRotateBlock::getHand,
          PacketRotateBlock::new);

  private final BlockPos pos;
  private final Direction side;
  private final InteractionHand hand;

  public PacketRotateBlock(BlockPos pos, Direction side, InteractionHand hand) {
    this.pos = pos;
    this.side = side;
    this.hand = hand;
  }

  public BlockPos getPos() { return pos; }
  public Direction getSide() { return side; }
  public InteractionHand getHand() { return hand; }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketRotateBlock msg, IPayloadContext context) {
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      Level level = player.level();
      boolean succ = BlockUtil.rotateBlockValidState(level, msg.pos, msg.side);
      if (succ) {
        ItemStack itemStackHeld = player.getItemInHand(msg.hand);
        ItemStackUtil.damageItem(player, itemStackHeld);
        if (level.getBlockState(msg.pos).getSoundType() != null) {
          SoundUtil.playSoundFromServer(player, level.getBlockState(msg.pos).getSoundType().getPlaceSound(), 1F, 1F);
        }
      }
    });
  }
}
