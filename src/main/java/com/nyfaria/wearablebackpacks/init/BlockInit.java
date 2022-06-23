package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.block.BackpackBlock;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nyfaria.wearablebackpacks.WearableBackpacks.MODID;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    public static final RegistryObject<Block> BACKPACK = BLOCKS.register("backpack", () -> new BackpackBlock(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BROWN).strength(2,3)));

    public static final RegistryObject<TileEntityType<?>> BACKPACK_BE = BLOCK_ENTITIES.register("backpack", () -> TileEntityType.Builder.of(BackpackBlockEntity::new, BACKPACK.get()).build(null));

}
