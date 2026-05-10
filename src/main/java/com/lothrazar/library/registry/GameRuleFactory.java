package com.lothrazar.library.registry;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Type;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

/**
 * accesstransformers.cfg:
 * 
 * 
 * public net.minecraft.world.level.GameRules$BooleanValue m_46250_(Z)Lnet/minecraft/world/level/GameRules$Type # create(boolean)
 * 
 * @author lothr
 *
 */
public class GameRuleFactory {

  private static final Logger LOGGER = LogManager.getLogger();

  public static Key<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
    return GameRules.register(id, cat, BooleanValue.create(defaultVal));
  }

}
