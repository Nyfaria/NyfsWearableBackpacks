package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagInit {

    public static final TagKey<Item> BLACKLIST = itemTag("backpack/blacklist");

    public static void init() {
    }

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(WearableBackpacks.MODID, path));
    }
}
