package com.lothrazar.library.mod;

import com.lothrazar.library.recipe.conditions.EntityExistsCondition;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlibRegistrations {

  public static final EntityExistsCondition.Serializer ENTITY_EXISTS = new EntityExistsCondition.Serializer();

  @SubscribeEvent
  public static void onRegistry(RegisterEvent event) {
    event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS,
        helper -> CraftingHelper.register(ENTITY_EXISTS));
  }
}
