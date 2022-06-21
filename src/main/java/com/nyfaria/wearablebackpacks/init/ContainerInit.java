package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.backpack.BackpackContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, WearableBackpacks.MODID);

    public static final RegistryObject<MenuType<BackpackContainer>> BACKPACK_CONTAINER = CONTAINERS.register("container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        int rows = 3;
        int columns = 9;
        BackpackInventory backpackInventory = new BackpackInventory(true);
        backpackInventory.deserializeNBT(data.readNbt());
        return new BackpackContainer(windowId, inv, backpackInventory, rows, columns, inv.player.blockPosition(), false, 0);
    }));
}
