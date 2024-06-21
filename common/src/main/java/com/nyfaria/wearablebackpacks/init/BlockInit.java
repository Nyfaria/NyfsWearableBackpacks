package com.nyfaria.wearablebackpacks.init;

import com.nyfaria.wearablebackpacks.block.BackpackBlock;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.registration.RegistrationProvider;
import com.nyfaria.wearablebackpacks.Constants;
import com.nyfaria.wearablebackpacks.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class BlockInit {
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);
    public static final RegistryObject<Block> BACKPACK = BLOCKS.register("backpack", () -> new BackpackBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(2,3)));


    public static final RegistryObject<BlockEntityType<BackpackBlockEntity>> BACKPACK_BE = BLOCK_ENTITIES.register("backpack", () -> BlockEntityType.Builder.of(BackpackBlockEntity::new, BACKPACK.get()).build(null));
    public static void loadClass() {
    }
}
