package com.nyfaria.wearablebackpacks;

import com.nyfaria.wearablebackpacks.cap.WornBackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.datagen.ModBlockStateProvider;
import com.nyfaria.wearablebackpacks.datagen.ModItemModelProvider;
import com.nyfaria.wearablebackpacks.datagen.ModLangProvider;
import com.nyfaria.wearablebackpacks.datagen.ModLootTableProvider;
import com.nyfaria.wearablebackpacks.datagen.ModRecipeProvider;
import com.nyfaria.wearablebackpacks.datagen.ModSoundProvider;
import com.nyfaria.wearablebackpacks.datagen.ModTagProvider;
import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Constants.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NyfsWearableBackpacks {
    
    public NyfsWearableBackpacks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BackpackConfig.CONFIG_SPEC);
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();
        WornBackpackHolderAttacher.register();
    }
    @SubscribeEvent
    public static void onFMLCommon(FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();
        generator.addProvider(includeServer, new ModRecipeProvider(packOutput));
        generator.addProvider(includeServer, new ModLootTableProvider(packOutput));
        generator.addProvider(includeServer, new ModSoundProvider(packOutput, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.Blocks(packOutput,event.getLookupProvider(), existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.Items(packOutput,event.getLookupProvider(), existingFileHelper));
        generator.addProvider(includeClient, new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModLangProvider(packOutput));
    }
}