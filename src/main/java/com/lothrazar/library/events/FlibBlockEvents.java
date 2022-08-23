package com.lothrazar.library.events;

import com.lothrazar.library.block.BlockFlib;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * NOT loaded by default load this into your event bus to pull in
 *
 *
 * Import @MinecraftForge and run
 * 
 * MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
 * 
 * inside of the event @FMLCommonSetupEvent
 */
public class FlibBlockEvents {

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    if (event.getItemStack().isEmpty()) {
      return;
    }
    BlockState stateHit = event.getWorld().getBlockState(event.getPos());
    if (stateHit.getBlock() instanceof BlockFlib block) {
      block.onRightClickBlock(event, stateHit);
    }
  }
}
