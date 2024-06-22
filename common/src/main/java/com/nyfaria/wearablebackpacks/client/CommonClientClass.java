package com.nyfaria.wearablebackpacks.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class CommonClientClass {
    public static KeyMapping OPEN_BACKPACK = new KeyMapping("key.wearablebackpacks.open", InputConstants.KEY_B, "key.categories.wearablebackpacks");
    public static final List<EntityType<? extends LivingEntity>> backpackHavers = List.of(
            EntityType.SKELETON,
            EntityType.WITHER_SKELETON,
            EntityType.STRAY,
            EntityType.ZOMBIE,
            EntityType.HUSK,
            EntityType.DROWNED,
            EntityType.PILLAGER,
            EntityType.VINDICATOR,
            EntityType.EVOKER,
            EntityType.ILLUSIONER,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.PLAYER
    );
    public static void init() {
        registerKeyBindings();
    }

    public static void registerKeyBindings(){
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings,OPEN_BACKPACK);
    }
}
