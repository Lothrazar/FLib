package com.lothrazar.library.registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;

public class GameRuleFactory {

  private static final Logger LOGGER = LogManager.getLogger();

  public static GameRule<Boolean> createBoolean(String id, boolean defaultVal, GameRuleCategory cat) {
    return GameRules.registerBoolean(id, cat, defaultVal);
  }

}
