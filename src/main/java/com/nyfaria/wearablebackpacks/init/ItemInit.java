package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleItemRenderer;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WearableBackpacks.MODID);

    public static final RegistryObject<BackpackItem> BACKPACK = ITEMS.register("backpack", () -> new BackpackItem(BlockInit.BACKPACK.get(),new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT).setISTER(()->SimpleItemRenderer::new)));
}
