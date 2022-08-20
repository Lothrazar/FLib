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
package com.lothrazar.library.util;

import java.lang.ref.WeakReference;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class FakePlayerUtil {

  private static final UUID ID = UUID.randomUUID();

  public static boolean isFakePlayer(Entity attacker) {
    return attacker instanceof FakePlayer;
  }

  public static WeakReference<FakePlayer> initFakePlayer(ServerLevel ws, String blockName) {
    final String name = "fake_player." + blockName;
    final GameProfile breakerProfile = new GameProfile(ID, name);
    WeakReference<FakePlayer> fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get(ws, breakerProfile));
    if (fakePlayer == null || fakePlayer.get() == null) {
      fakePlayer = null;
      return null; // trying to get around https://github.com/PrinceOfAmber/Cyclic/issues/113
    }
    fakePlayer.get().setOnGround(true);
    //    fakePlayer.get().onGround = true;
    fakePlayer.get().connection = new ServerGamePacketListenerImpl(ws.getServer(), new Connection(PacketFlow.SERVERBOUND), fakePlayer.get()) {

      @Override
      public void send(Packet<?> packetIn) {}
    };
    fakePlayer.get().setSilent(true);
    return fakePlayer;
  }

  public static void tryEquipItem(LazyOptional<IItemHandler> i, WeakReference<FakePlayer> fp, int slot, InteractionHand hand) {
    if (fp == null) {
      return;
    }
    i.ifPresent(inv -> {
      ItemStack maybeTool = inv.getStackInSlot(0);
      if (!maybeTool.isEmpty()) {
        if (maybeTool.getCount() <= 0) {
          maybeTool = ItemStack.EMPTY;
        }
      }
      if (!maybeTool.equals(fp.get().getItemInHand(hand))) {
        fp.get().setItemInHand(hand, maybeTool);
      }
    });
  }

  public static InteractionResult interactUseOnBlock(WeakReference<FakePlayer> fakePlayer,
      Level world, BlockPos targetPos, InteractionHand hand, Direction facing) throws Exception {
    if (fakePlayer == null) {
      return InteractionResult.FAIL;
    }
    Direction placementOn = (facing == null) ? fakePlayer.get().getMotionDirection() : facing;
    BlockHitResult blockraytraceresult = new BlockHitResult(
        fakePlayer.get().getLookAngle(), placementOn,
        targetPos, true);
    //processRightClick
    ItemStack itemInHand = fakePlayer.get().getItemInHand(hand);
    InteractionResult result = fakePlayer.get().gameMode.useItemOn(fakePlayer.get(), world, itemInHand, hand, blockraytraceresult);
    // ModCyclic.LOGGER.info(targetPos + " gameMode.useItemOn() result = " + result + "  itemInHand = " + itemInHand);
    //it becomes CONSUME result 1 bucket. then later i guess it doesnt save, and then its water_bucket again
    return result;
  }

  public static void syncEquippedItem(ItemStackHandler inv, WeakReference<FakePlayer> fp, int slot, InteractionHand hand) {
    if (fp == null) {
      return;
    }
    inv.setStackInSlot(slot, ItemStack.EMPTY);
    //    inv.extractItem(slot, 64, false); //delete and overwrite
    inv.insertItem(slot, fp.get().getItemInHand(hand), false);
  }

  public static void tryEquipItem(ItemStack item, WeakReference<FakePlayer> fp, InteractionHand hand) {
    if (fp == null) {
      return;
    }
    fp.get().setItemInHand(hand, item);
  }
}
