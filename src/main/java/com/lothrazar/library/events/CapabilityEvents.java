package com.lothrazar.library.events;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.cap.player.PlayerCapProvider;
import com.lothrazar.library.cap.player.PlayerCapabilityStorage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * NOT loaded by default. Call {@link #register(IEventBus)} from inside your @Mod constructor to enable
 * the FLib player mana capability.
 *
 * Example:
 *   CapabilityEvents.register(modEventBus);
 */
public class CapabilityEvents {

  public static void register(IEventBus modBus) {
    NeoForge.EVENT_BUS.addListener(CapabilityEvents::onPlayerCloned);
    modBus.addListener(CapabilityEvents::onRegisterCapabilities);
    FutureLibMod.LOGGER.info("CapabilityEvents registered");
  }

  public static void onPlayerCloned(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      PlayerCapabilityStorage oldData = event.getOriginal().getData(PlayerCapProvider.PLAYER_MANA.get());
      event.getEntity().getData(PlayerCapProvider.PLAYER_MANA.get()).copyFrom(oldData);
    }
  }

  public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.registerEntity(
        PlayerCapProvider.PLAYER_MANA_CAP,
        (entity, ctx) -> entity instanceof Player ? entity.getData(PlayerCapProvider.PLAYER_MANA.get()) : null,
        EntityType.PLAYER);
    FutureLibMod.LOGGER.info("RegisterCapabilitiesEvent success for PlayerMana");
  }
}
