package com.lothrazar.library.core;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public interface IEntityInteractable {

  /**
   * Interact with your item when this event is triggered
   * 
   * @param event
   *          for the entity interaction on this
   */
  void interactWith(PlayerInteractEvent.EntityInteract event);
}
