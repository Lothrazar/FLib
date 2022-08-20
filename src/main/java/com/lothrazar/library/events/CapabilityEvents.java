package com.lothrazar.library.events;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.cap.player.PlayerCapProvider;
import com.lothrazar.library.cap.player.PlayerCapabilityStorage;
import com.lothrazar.library.cap.world.FlibWorldSavedData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
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

  @SubscribeEvent
  public void onWorldTick(TickEvent.WorldTickEvent event) {
    // Don't do anything client side
    if (event.world.isClientSide) {
      return;
    }
    if (event.phase == TickEvent.Phase.START) {
      return;
    }
    //ok then tick the manager and sync
    // Get the mana manager for this level
    FlibWorldSavedData manager = FlibWorldSavedData.get(event.world);
    manager.onWorldTick(event.world);
  }
  // When a player dies or teleports from the end capabilities are cleared. Using the PlayerEvent.Clone event
  // we can detect this and copy our capability from the old player to the new one

  @SubscribeEvent
  public void onPlayerCloned(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      // We need to copyFrom the capabilities
      event.getOriginal().getCapability(PlayerCapProvider.CYCLIC_PLAYER).ifPresent(oldStore -> {
        event.getPlayer().getCapability(PlayerCapProvider.CYCLIC_PLAYER).ifPresent(newStore -> {
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
      if (!event.getObject().getCapability(PlayerCapProvider.CYCLIC_PLAYER).isPresent()) {
        // The player does not already have this capability so we need to add the capability provider here
        //TODO: Data keyword string from IMC? 
        event.addCapability(new ResourceLocation(FutureLibMod.MODID, "data"), new PlayerCapProvider());
        FutureLibMod.LOGGER.info("CapabilityRegistry success for data");
      }
    }
  }
  // CLIENT OVERLAY STUFF
  // see @IIngameOverlay 
  // call from @FMLClientSetupEvent event
  //    OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "data", HUD_MANA);
  //  public static final IIngameOverlay HUD_MANA = (gui, poseStack, partialTicks, width, height) -> {
  //    //cancel if turned off
  //    if (!FeatureRegistry.PLAYER_RENDER_CAPS) {
  //      return;
  //    }
  //    //ok go
  //    if (Minecraft.getInstance().player.getMainHandItem().is(ItemRegistry.BATTERY_INFINITE.get())) {
  //      final String toDisplay = "P:" + ClientDataManager.getPlayerMana() + " CH:" + ClientDataManager.getChunkMana();
  //      int x = 10; // ManaConfig.MANA_HUD_X.get();
  //      int y = 10; // ManaConfig.MANA_HUD_Y.get(); //TODO: client-config
  //      if (x >= 0 && y >= 0) {
  //        gui.getFont().draw(poseStack, toDisplay, x, y, 0xFF0000); // client config color
  //      }
  //    }
  //  };
}
