package com.lothrazar.library.mod;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.config.ConfigTemplate;
//import net.minecraftforge.common.ForgeConfigSpec;
//import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
//import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.*;

public class ConfigModule extends ConfigTemplate {

  static final ModConfigSpec SPEC;
  private static final BooleanValue ENABLE_COMMANDS;
  public static boolean enableCommands;

  static {
    final Builder builder = builder();
    builder.comment("General settings").push(FutureLibMod.MODID);
    ENABLE_COMMANDS = builder.comment("If true, the /flib command will be registered").define("command.enabled", true);
    builder.pop(); // one pop for every push
    SPEC = builder.build();
  }
  @SubscribeEvent
  static void onLoad(final ModConfigEvent event)  {
    enableCommands = ENABLE_COMMANDS.get();
  }
  public ConfigModule() {
//    CONFIG.setConfig(setup(FutureLibMod.MODID));
  }
}
