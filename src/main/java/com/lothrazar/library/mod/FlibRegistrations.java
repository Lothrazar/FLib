package com.lothrazar.library.mod;

import com.lothrazar.library.recipe.conditions.EntityExistsCondition;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlibRegistrations {

  public static final EntityExistsCondition.Serializer ENTITY_EXISTS = new EntityExistsCondition.Serializer();

  @SubscribeEvent
  public static void onRegistry(RegisterEvent event) {
    event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS,
        helper -> CraftingHelper.register(ENTITY_EXISTS));
  }
}
