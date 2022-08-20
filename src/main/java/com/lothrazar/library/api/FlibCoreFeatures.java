package com.lothrazar.library.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import com.lothrazar.library.FutureLibMod;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

/**
 * Coremod features, mixin features, etc
 * 
 *
 */
public enum FlibCoreFeatures {

  MAP, COMMANDS;

  @Override
  public String toString() {
    return this.name().toUpperCase();
  }

  public static Map<FlibCoreFeatures, Supplier<Boolean>> INSTANCE = new HashMap<>();
  static {
    for (FlibCoreFeatures f : FlibCoreFeatures.values()) {
      INSTANCE.put(f, () -> false);
    }
  }

  public static int executeCommand(CommandContext<CommandSourceStack> x, String string, boolean bool) {
    FutureLibMod.LOGGER.error("CMD instance override " + string);
    FlibCoreFeatures maybeNull = FlibCoreFeatures.valueOf(string.toUpperCase());
    FutureLibMod.LOGGER.error(maybeNull + "CMD instance, previous was " + INSTANCE.get(maybeNull));
    INSTANCE.put(maybeNull, () -> bool);
    return 0;
  }

  /**
   * Do not persist the return value, check if the current supplier allows this feature
   * 
   * @return true of a valid supplier is found and has this value
   */
  public boolean currentlyEnabled() {
    return INSTANCE.containsKey(this) ? INSTANCE.get(this).get() : false;
  }
}
