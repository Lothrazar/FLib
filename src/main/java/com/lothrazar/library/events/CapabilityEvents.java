package com.lothrazar.library.events;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.cap.player.PlayerCapProvider;
import com.lothrazar.library.cap.player.PlayerCapabilityStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
public class CapabilityEvents {

  public CapabilityEvents() {
    MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityEvents::onAttachCapabilitiesPlayer);
  }
  // When a player dies or teleports from the end capabilities are cleared. Using the PlayerEvent.Clone event
  // we can detect this and copy our capability from the old player to the new one

  @SubscribeEvent
  public void onPlayerCloned(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      // We need to copyFrom the capabilities
      event.getOriginal().getCapability(PlayerCapProvider.PLAYERCAP).ifPresent(oldStore -> {
        event.getEntity().getCapability(PlayerCapProvider.PLAYERCAP).ifPresent(newStore -> {
          newStore.copyFrom(oldStore);
        });
      });
    }
  }

  @SubscribeEvent
  public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.register(PlayerCapabilityStorage.class);
    FutureLibMod.LOGGER.info("RegisterCapabilitiesEvent success for ManaManager");
  }
  // Whenever a new object of some type is created the AttachCapabilitiesEvent will fire. In our case we want to know
  // when a new player arrives so that we can attach our capability here

  //  @SubscribeEvent not sub, uses  MinecraftForge.EVENT_BUS.addGenericListener instead
  public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player) {
      if (!event.getObject().getCapability(PlayerCapProvider.PLAYERCAP).isPresent()) {
        // The player does not already have this capability so we need to add the capability provider here
        //TODO: Data keyword string from IMC? 
        event.addCapability(new ResourceLocation(FutureLibMod.MODID, "data"), new PlayerCapProvider());
        FutureLibMod.LOGGER.info("CapabilityRegistry success for data");
      }
    }
  }
}
