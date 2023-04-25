package com.lothrazar.library.events;

import com.lothrazar.library.block.BlockFlib;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FlibBlockEvents extends EventFlib {

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    if (event.getItemStack().isEmpty()) {
      return;
    }
    BlockState stateHit = event.getLevel().getBlockState(event.getPos());
    if (stateHit.getBlock() instanceof BlockFlib block) {
      block.onRightClickBlock(event, stateHit);
    }
  }
}
