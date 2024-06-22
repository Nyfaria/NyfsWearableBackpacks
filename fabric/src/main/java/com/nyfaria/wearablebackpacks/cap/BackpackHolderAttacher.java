package com.nyfaria.wearablebackpacks.cap;

import com.nyfaria.wearablebackpacks.Constants;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;

public class BackpackHolderAttacher implements EntityComponentInitializer {
    public static final ComponentKey<WornBackpackHolder> WORN_BACKPACK = ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(Constants.MODID, "backpack"), WornBackpackHolder.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PathfinderMob.class,WORN_BACKPACK, WornBackpackHolder::new);
        registry.registerForPlayers(WORN_BACKPACK, WornBackpackHolder::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
