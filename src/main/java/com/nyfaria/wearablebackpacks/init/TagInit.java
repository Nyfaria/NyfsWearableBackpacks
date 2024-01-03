package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class TagInit {

    public static final TagKey<Item> BLACKLIST = itemTag("backpack/blacklist");
    public static final TagKey<EntityType<?>> BACKPACKABLE = entityTag("backpack");

    public static void init() {
    }

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(WearableBackpacks.MODID, path));
    }
    private static TagKey<EntityType<?>> entityTag(String pName) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(WearableBackpacks.MODID,pName));
    }
}
