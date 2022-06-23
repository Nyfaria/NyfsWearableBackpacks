package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.backpack.BackpackBEContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, WearableBackpacks.MODID);

    public static final RegistryObject<ContainerType<BackpackContainer>> BACKPACK_CONTAINER = CONTAINERS.register("container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BackpackInventory backpackInventory = new BackpackInventory(true);
        backpackInventory.deserializeNBT(data.readNbt());
        return new BackpackContainer(windowId, inv, backpackInventory, inv.player.blockPosition(), false, 0);
    }));
    public static final RegistryObject<ContainerType<BackpackBEContainer>> BACKPACK_BE_CONTAINER = CONTAINERS.register("container_be", () -> IForgeContainerType.create((windowId, inv, data) -> new BackpackBEContainer(windowId, inv)));
}
