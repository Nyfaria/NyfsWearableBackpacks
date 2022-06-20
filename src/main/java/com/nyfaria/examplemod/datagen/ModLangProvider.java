package com.nyfaria.examplemod.datagen;

import com.google.common.collect.ImmutableMap;
import com.nyfaria.examplemod.init.BlockInit;
import com.nyfaria.examplemod.init.EntityInit;
import com.nyfaria.examplemod.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
    }


    protected void itemLang(Supplier<Item> entry) {

        List<String> words = new ArrayList<>();
        Arrays.stream(entry.get().getRegistryName().getPath().split("_")).toList().forEach(e -> {

                    words.add(checkReplace(e));

                }
        );
        if (!(entry.get() instanceof BlockItem)) {
            addItem(entry, String.join(" ", words).trim());
        }
    }

    protected void blockLang(Supplier<Block> entry) {
        List<String> words = new ArrayList<>();
        Arrays.stream(entry.get().getRegistryName().getPath().split("_")).toList().forEach(e -> {

                    words.add(checkReplace(e));

                }
        );
        if (!(entry instanceof BlockItem)) {
            addBlock(entry, String.join(" ", words).trim());
        }
    }

    protected void entityLang(Supplier<EntityType<?>> entry) {
        List<String> words = new ArrayList<>();
        Arrays.stream(entry.get().getRegistryName().getPath().split("_")).toList().forEach(e -> {

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
