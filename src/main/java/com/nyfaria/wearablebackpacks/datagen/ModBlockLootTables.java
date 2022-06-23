package com.nyfaria.wearablebackpacks.datagen;

import com.nyfaria.wearablebackpacks.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootTables {
    @Override
    protected void addTables() {
        BlockInit.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> block.asItem() != Items.AIR && !(block instanceof OreBlock))
                .forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.asItem() != Items.AIR && !(block instanceof OreBlock)).collect(Collectors.toList());
    }

}
