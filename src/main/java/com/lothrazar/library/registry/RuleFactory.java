package com.lothrazar.library.registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;

public class RuleFactory {

  public static final Logger LOGGER = LogManager.getLogger();

  public static GameRule<Boolean> createBoolean(String id, boolean defaultVal, GameRuleCategory cat) {
    LOGGER.info("Attempting to register a new gamerule : " + id);
    return GameRules.registerBoolean(id, cat, defaultVal);
  }
}
