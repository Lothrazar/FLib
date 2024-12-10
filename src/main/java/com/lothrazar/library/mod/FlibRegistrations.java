package com.lothrazar.library.mod;

import com.lothrazar.library.recipe.conditions.EntityExistsCondition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.crafting.CraftingHelper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FlibRegistrations {

//  public static final EntityExistsCondition.Serializer ENTITY_EXISTS = new EntityExistsCondition.Serializer();

  @SubscribeEvent
  public static void onRegistry(RegisterEvent event) {
    System.out.println("TODO: register ENTITY_EXISTS recipe condition serializer/codec");
//    event.register(NeoForgeRegistries.Keys.RECIPE_SERIALIZERS,
//        helper -> CraftingHelper.register(ENTITY_EXISTS));
  }
}
