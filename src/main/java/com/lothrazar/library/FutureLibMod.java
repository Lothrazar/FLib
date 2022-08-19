package com.lothrazar.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.library.module.CommandModule;
import com.lothrazar.library.module.ConfigModule;
import net.minecraftforge.common.MinecraftForge;
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
  //1: tpdim (teleport dim) to test dimension transit

  private void setup(final FMLCommonSetupEvent event) {
    //   placeholder
  }

  private void setupClient(final FMLClientSetupEvent event) {
    //   placeholder
  }
}
