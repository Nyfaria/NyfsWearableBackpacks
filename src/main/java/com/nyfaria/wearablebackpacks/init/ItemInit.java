package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WearableBackpacks.MODID);

    public static final RegistryObject<Item> BACKPACK = ITEMS.register("backpack", () -> new BackpackItem(BlockInit.BACKPACK.get(),new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT)));
}
