package com.nyfaria.wearablebackpacks;

import com.nyfaria.wearablebackpacks.cap.BackpackBEHolderAttacher;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.cap.WornBackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.datagen.ModBlockStateProvider;
import com.nyfaria.wearablebackpacks.datagen.ModItemModelProvider;
import com.nyfaria.wearablebackpacks.datagen.ModLangProvider;
import com.nyfaria.wearablebackpacks.datagen.ModLootTableProvider;
import com.nyfaria.wearablebackpacks.datagen.ModRecipeProvider;
import com.nyfaria.wearablebackpacks.datagen.ModSoundProvider;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ContainerInit;
import com.nyfaria.wearablebackpacks.init.EntityInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.init.TagInit;
import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        WornBackpackHolderAttacher.register();

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
