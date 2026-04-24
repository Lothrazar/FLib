package com.lothrazar.library;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.library.events.FlibBlockEvents;
import com.lothrazar.library.mod.CommandModule;
import com.lothrazar.library.mod.ConfigModule;
import com.lothrazar.library.mod.PacketRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
//import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FutureLibMod.MODID)
public class FutureLibMod {

  public static final String MODID = "flib";
  public static final Logger LOGGER = LogManager.getLogger();

  /**
   * all "content" of the mod such as built in commands are in the 'library.mod' package, to keep it split up from the rest of the library code
   */
  public FutureLibMod(IEventBus bus, ModContainer modContainer) {
    new ConfigModule();
    new CommandModule();
    new FlibBlockEvents();
    com.lothrazar.library.registry.FlibDataComponents.DATA_COMPONENT_TYPES.register(bus);
    bus.addListener(this::setup);
    bus.addListener(PacketRegistry::register);
  }

  private void setup(final FMLCommonSetupEvent event) {
    PacketRegistry.setup();
    InterModComms.getMessages(MODID).forEach(x -> {
      LOGGER.info("registration from " + x.senderModId() + " | " + x.messageSupplier().get());
    });
  }


  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(MODID, path);
  }
}
