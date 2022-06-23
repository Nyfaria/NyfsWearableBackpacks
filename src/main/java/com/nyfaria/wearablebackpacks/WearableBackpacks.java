package com.nyfaria.wearablebackpacks;

import com.nyfaria.wearablebackpacks.cap.BackpackBEHolderAttacher;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.datagen.*;
import com.nyfaria.wearablebackpacks.init.*;
import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WearableBackpacks.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WearableBackpacks {
    public static final String MODID = "wearablebackpacks";
    public static final Logger LOGGER = LogManager.getLogger();

    public WearableBackpacks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BackpackConfig.CONFIG_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        TagInit.init();
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        ContainerInit.CONTAINERS.register(bus);
        BackpackHolderAttacher.register();
        BackpackBEHolderAttacher.register();

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.register();
        event.enqueueWork(() -> {
            CauldronInteraction.WATER.put(ItemInit.BACKPACK.get(), CauldronInteraction.DYED_ITEM);
        });
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(generator));
        generator.addProvider(event.includeServer(), new ModSoundProvider(generator, MODID, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(generator, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(generator, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLangProvider(generator, MODID, "en_us"));
    }
}
