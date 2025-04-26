package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.Constants;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.registration.RegistrationProvider;
import com.nyfaria.wearablebackpacks.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemInit {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MODID);
    public static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(Constants.MODID, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ItemInit.BACKPACK.get()))
            .displayItems(
                    (itemDisplayParameters, output) -> {
                        Arrays.stream(DyeColor.values()).toList().forEach(dyeColor -> {
                            ItemStack stack = new ItemStack(ItemInit.BACKPACK.get());
                            stack = DyeableLeatherItem.dyeArmor(stack, List.of(DyeItem.byColor(dyeColor)));
                            output.accept(stack);
                        });
                    }).title(Component.translatable("itemGroup." + Constants.MODID + ".tab"))
            .build());

    public static final RegistryObject<BackpackItem> BACKPACK = ITEMS.register("backpack", () -> new BackpackItem(BlockInit.BACKPACK.get(),new Item.Properties().stacksTo(1)));
    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }

    public static void loadClass() {
    }
}
