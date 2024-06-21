package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.Constants;
import com.nyfaria.wearablebackpacks.backpack.BackpackBEMenu;
import com.nyfaria.wearablebackpacks.backpack.BackpackMenu;
import com.nyfaria.wearablebackpacks.platform.Services;
import com.nyfaria.wearablebackpacks.registration.RegistrationProvider;
import com.nyfaria.wearablebackpacks.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class MenuInit {
    public static final RegistrationProvider<MenuType<?>> MENUS = RegistrationProvider.get(BuiltInRegistries.MENU, Constants.MODID);

    public static final RegistryObject<MenuType<BackpackMenu>> BACKPACK_MENU = MENUS.register("container", Services.PLATFORM::registerBPMenu);
    public static final RegistryObject<MenuType<BackpackBEMenu>> BACKPACK_BE_MENU = MENUS.register("container_be", Services.PLATFORM::registerBPBEMenu);

    public static void loadClass() {

    }
}
