package com.lothrazar.library.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
//import net.minecraftforge.network.NetworkEvent.Context;

public interface PacketFlib extends CustomPacketPayload {
  public static final StreamCodec<ByteBuf, InteractionHand> INTERACTION_HAND_STREAM_CODEC = enumCodec(InteractionHand.class);
  public static final StreamCodec<ByteBuf, EquipmentSlot> EQUIPMENT_SLOT_STREAM_CODEC = enumCodec(EquipmentSlot.class);
  public static final StreamCodec<ByteBuf, Direction> DIRECTION_SLOT_STREAM_CODEC = enumCodec(Direction.class);
  public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.DOUBLE, Vec3::x,
          ByteBufCodecs.DOUBLE, Vec3::y,
          ByteBufCodecs.DOUBLE, Vec3::z,
          Vec3::new
  );
//  public static final StreamCodec<ByteBuf, Vec3> BLOCKPOS_STREAM_CODEC = StreamCodec.composite(
//          ByteBufCodecs.DOUBLE, BlockPos::x,
//          ByteBufCodecs.DOUBLE, BlockPos::y,
//          ByteBufCodecs.DOUBLE, BlockPos::z,
//          BlockPos::new
//  );

  //thank you to mekanism for this hotfix
  //Similar to NeoForgeStreamCodecs#enumCodec but allows for keeping it as a ByteBuf and wrapping the value
  public static <V extends Enum<V>> StreamCodec<ByteBuf, V> enumCodec(Class<V> enumClass) {
    return ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, enumClass.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.WRAP), Enum::ordinal);

  }
  void handle(IPayloadContext context);


//  public void done(Supplier<Context> ctx) {
//    ctx.get().setPacketHandled(true);
//  }
}
