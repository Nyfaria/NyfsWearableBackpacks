package com.nyfaria.wearablebackpacks.datagen;

import com.google.common.collect.ImmutableMap;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.EntityInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLangProvider extends LanguageProvider {

    protected static final Map<String,String> REPLACE_LIST = ImmutableMap.of(
            "tnt","TNT",
            "sus",""
    );

    public ModLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        ItemInit.ITEMS.getEntries().forEach(this::itemLang);
        EntityInit.ENTITIES.getEntries().forEach(this::entityLang);
        BlockInit.BLOCKS.getEntries().forEach(this::blockLang);
        add("message.wearablebackpacks.limit","You already have a backpack equipped.");
        add("message.wearablebackpacks.chestplate","You can't equip a backpack while wearing a chestplate.");
        add("message.wearablebackpacks.sneak_place","Sneak and right click ground \nwith empty hand to unequip.");
        add("message.wearablebackpacks.place","Place down and break \nwhile sneaking to equip");
        add("keys.category.wearablebackpacks","Wearable Backpacks");
        add("keys.wearablebackpacks.open_backpack","Open Backpack");
    }


    protected void itemLang(Supplier<Item> entry) {

        List<String> words = new ArrayList<>();
        Arrays.stream(entry.get().getRegistryName().getPath().split("_")).collect(Collectors.toList()).forEach(e -> words.add(checkReplace(e))
        );
        if (!(entry.get() instanceof BlockItem)) {
            addItem(entry, String.join(" ", words).trim());
        }
    }

    protected void blockLang(Supplier<Block> entry) {
        List<String> words = new ArrayList<>();
        Arrays.stream(entry.get().getRegistryName().getPath().split("_")).collect(Collectors.toList()).forEach(e -> {

                    words.add(checkReplace(e));

                }
        );
        if (!(entry instanceof BlockItem)) {
            addBlock(entry, String.join(" ", words).trim());
        }
    }

    protected void entityLang(Supplier<EntityType<?>> entry) {
        List<String> words = new ArrayList<>();
        Arrays.stream(entry.get().getRegistryName().getPath().split("_")).collect(Collectors.toList()).forEach(e -> {

                    words.add(checkReplace(e));

                }
        );
        if (!(entry instanceof BlockItem)) {
            addEntityType(entry, String.join(" ", words).trim());
        }
    }

    protected String checkReplace(String string) {
        if (REPLACE_LIST.keySet().contains(string)) {
            return REPLACE_LIST.get(string);
        } else {
            return StringUtils.capitalize(string);
        }
    }
}
