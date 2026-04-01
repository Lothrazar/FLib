package com.lothrazar.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.library.cap.player.PlayerCapProvider;
import com.lothrazar.library.events.FlibBlockEvents;
import com.lothrazar.library.mod.CommandModule;
import com.lothrazar.library.mod.ConfigModule;
import com.lothrazar.library.mod.FlibRegistrations;
import com.lothrazar.library.mod.PacketRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(FutureLibMod.MODID)
public class FutureLibMod {

  public static final String MODID = "flib";
  public static final Logger LOGGER = LogManager.getLogger();

  public FutureLibMod(IEventBus modEventBus, ModContainer modContainer) {
    new ConfigModule(modContainer);
    new CommandModule();
    new FlibBlockEvents();
    PlayerCapProvider.ATTACHMENT_TYPES.register(modEventBus);
    FlibRegistrations.register(modEventBus);
    modEventBus.addListener(PacketRegistry::onRegisterPayloads);
  }
}
