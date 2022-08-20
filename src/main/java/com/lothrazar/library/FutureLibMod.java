package com.lothrazar.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.library.api.FlibCoreFeatures;
import com.lothrazar.library.module.CommandModule;
import com.lothrazar.library.module.ConfigModule;
import com.lothrazar.library.registry.PacketRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FutureLibMod.MODID)
public class FutureLibMod {

  public static final String MODID = "flib";
  public static final Logger LOGGER = LogManager.getLogger();

  public FutureLibMod() {
    ConfigModule.setup();
    MinecraftForge.EVENT_BUS.register(new CommandModule());
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
  }

  //TODO: add command!!
  // mostly for testing
  //1:  /flib tpx <dim> @p x y z
  //    /flib tpxsave <dim> @p x y z
  //    /flib tpxback @p(teleport dim) to test dimension transit
  //     make it save. aka TP save
  //then add a tp/back . goes back to previous spot and also clears it
  // IMC . any other mod TO flib 
  // gamerule mixin
  //elementary ores
  //
  private void setup(final FMLCommonSetupEvent event) {
    PacketRegistry.setup();
    InterModComms.getMessages(MODID).forEach(x -> {
      LOGGER.info("registration from " + x.senderModId() + " | " + x.messageSupplier().get());
    });
    //set features hooked to config
    FlibCoreFeatures.INSTANCE.put(FlibCoreFeatures.COMMANDS, () -> ConfigModule.ENABLE_COMMANDS.get());
  }

  private void setupClient(final FMLClientSetupEvent event) {
    //   placeholder
  }
}
