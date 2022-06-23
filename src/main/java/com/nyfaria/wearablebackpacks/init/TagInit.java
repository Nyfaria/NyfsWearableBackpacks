package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class TagInit {

    public static final ITag.INamedTag<Item> BLACKLIST = itemTag("backpack/blacklist");
    public static final ITag.INamedTag<EntityType<?>> BACKPACKABLE = entityTag("backpack");

    public static void init() {
    }

    private static ITag.INamedTag<Item> itemTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(WearableBackpacks.MODID, path));
    }
    private static ITag.INamedTag<EntityType<?>> entityTag(String pName) {
        return EntityTypeTags.createOptional(new ResourceLocation(WearableBackpacks.MODID,pName));
    }
}
