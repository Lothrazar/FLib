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
import com.lothrazar.library.core.IHasEnergy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Forge docs suggest using a direct packet to keep capabilities, such as power, in sync with the client according to https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/
 */
public record PacketSyncEnergy(
        BlockPos pos,
        int energy
) implements PacketFlib {

  public static final CustomPacketPayload.Type<PacketSyncEnergy> TYPE = new CustomPacketPayload.Type<>(FutureLibMod.rl( "sync_energy"));
  public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketSyncEnergy> STREAM_CODEC = StreamCodec.composite(
          BlockPos.STREAM_CODEC, PacketSyncEnergy::pos,
          ByteBufCodecs.INT, PacketSyncEnergy::energy,
          PacketSyncEnergy::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handle(IPayloadContext context) {

    BlockEntity te = Minecraft.getInstance().level.getBlockEntity(this.pos());
    if (te instanceof IHasEnergy tile) {
      tile.setEnergy(this.energy());
    }
  }
}
