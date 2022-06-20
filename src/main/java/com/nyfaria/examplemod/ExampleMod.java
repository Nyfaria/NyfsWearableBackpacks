package com.nyfaria.examplemod;

import com.nyfaria.examplemod.cap.ExampleHolderAttacher;
import com.nyfaria.examplemod.datagen.*;
import com.nyfaria.examplemod.init.BlockInit;
import com.nyfaria.examplemod.init.EntityInit;
import com.nyfaria.examplemod.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExampleMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExampleMod {
    public static final String MODID = "examplemod";
    public static final Logger LOGGER = LogManager.getLogger();

    public ExampleMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        ExampleHolderAttacher.register();

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new ModRecipeProvider(generator));
            generator.addProvider(new ModLootTableProvider(generator));
            generator.addProvider(new ModSoundProvider(generator, MODID,existingFileHelper));
        }
        if (event.includeClient()) {
            generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
            generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
            generator.addProvider(new ModLangProvider(generator, MODID, "en_us"));
        }
    }
}
