package com.lothrazar.library.mod;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.recipe.conditions.EntityExistsCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FlibRegistrations {

  private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
      DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, FutureLibMod.MODID);

  static {
    CONDITIONS.register("entity_exists", () -> EntityExistsCondition.CODEC);
  }

  public static void register(IEventBus modBus) {
    CONDITIONS.register(modBus);
  }
}
