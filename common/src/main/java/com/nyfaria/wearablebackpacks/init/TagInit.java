package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class TagInit {

    public static final TagKey<Item> BLACKLIST = itemTag("backpack/blacklist");
    public static final TagKey<EntityType<?>> BACKPACKABLE = entityTag("backpack");

    public static void init() {
    }

    private static TagKey<Item> itemTag(String path) {
        return TagKey.create(BuiltInRegistries.ITEM.key(),new ResourceLocation(Constants.MODID, path));
    }
    private static TagKey<EntityType<?>> entityTag(String pName) {
        return TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), new ResourceLocation(Constants.MODID,pName));
    }
}