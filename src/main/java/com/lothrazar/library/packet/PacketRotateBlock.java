/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.library.packet;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.util.BlockUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record  PacketRotateBlock(
        BlockPos pos,
        Direction side,
        InteractionHand hand
) implements PacketFlib {

  public static final CustomPacketPayload.Type<PacketPlayerFalldamage> TYPE = new CustomPacketPayload.Type<>(FutureLibMod.rl( "rotate_block"));
  public static final StreamCodec<ByteBuf, PacketRotateBlock> STREAM_CODEC = StreamCodec.composite(
          BlockPos.STREAM_CODEC, PacketRotateBlock::pos,
          DIRECTION_SLOT_STREAM_CODEC, PacketRotateBlock::side,
          INTERACTION_HAND_STREAM_CODEC, PacketRotateBlock::hand,
          PacketRotateBlock::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handle(IPayloadContext context) {
      //rotate type
      Player player = context.player();
      Level level = player.level();
      boolean succ = BlockUtil.rotateBlockValidState(level, this.pos(), this.side());
      if (succ && player instanceof ServerPlayer sp) {
        ItemStack itemStackHeld = player.getItemInHand(this.hand());
        ItemStackUtil.damageItem(player, itemStackHeld);
        if (level.getBlockState(this.pos()).getSoundType() != null) {
          SoundUtil.playSoundFromServer(sp, level.getBlockState(this.pos()).getSoundType().getPlaceSound(), 1F, 1F);
        }
      }
  }
}
