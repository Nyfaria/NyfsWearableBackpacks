package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.block.BackpackBlock;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nyfaria.wearablebackpacks.WearableBackpacks.MODID;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final RegistryObject<Block> BACKPACK = BLOCKS.register("backpack", () -> new BackpackBlock(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.COLOR_BROWN).strength(2,3)));

    public static final RegistryObject<BlockEntityType<BackpackBlockEntity>> BACKPACK_BE = BLOCK_ENTITIES.register("backpack", () -> BlockEntityType.Builder.of(BackpackBlockEntity::new, BACKPACK.get()).build(null));

}
