package com.lothrazar.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.library.events.FlibBlockEvents;
import com.lothrazar.library.mod.CommandModule;
import com.lothrazar.library.mod.ConfigModule;
import com.lothrazar.library.mod.PacketRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FutureLibMod.MODID)
public class FutureLibMod {

  public static final String MODID = "flib";
  public static final Logger LOGGER = LogManager.getLogger();

  /**
   * all "content" of the mod such as built in commands are in the 'library.mod' package, to keep it split up from the rest of the library code
   */
  public FutureLibMod() {
    new ConfigModule();
    new CommandModule();
    new FlibBlockEvents();
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(this::setup);
    bus.addListener(this::setupClient);
  }

  private void setupClient(final FMLClientSetupEvent event) {
    //   placeholder/example
  }

  private void setup(final FMLCommonSetupEvent event) {
    PacketRegistry.setup();
    InterModComms.getMessages(MODID).forEach(x -> {
      LOGGER.info("registration from " + x.senderModId() + " | " + x.messageSupplier().get());
    });
  }
}
