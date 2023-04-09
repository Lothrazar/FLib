package com.lothrazar.library.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegistryFactory {

  public static void buildTab(CreativeModeTabEvent.Register event, String modid, Item icon, DeferredRegister<Item> items) {
    event.registerCreativeModeTab(new ResourceLocation(modid, "tab"), builder -> builder
        .title(Component.translatable("itemGroup." + modid))
        .icon(() -> new ItemStack(icon))
        .displayItems((enabledFlags, populator) -> {
          for (RegistryObject<Item> b : items.getEntries()) {
            populator.accept(b.get());
          }
        }));
  }
}
