package com.nyfaria.wearablebackpacks.event;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleArmorRenderer;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleBlockEntityRenderer;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ContainerInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.screens.BackpackContainerScreen;
import com.nyfaria.wearablebackpacks.tooltip.ClientBackpackTooltip;
import com.nyfaria.wearablebackpacks.tooltip.BackpackTooltip;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = WearableBackpacks.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        MenuScreens.register(ContainerInit.BACKPACK_CONTAINER.get(), BackpackContainerScreen::new);
        ItemBlockRenderTypes.setRenderLayer(BlockInit.BACKPACK.get(), RenderType.cutout());
        MinecraftForgeClient.registerTooltipComponentFactory(BackpackTooltip.class, ClientBackpackTooltip::new);
        GeoArmorRenderer.registerArmorRenderer(BackpackItem.class, SimpleArmorRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockInit.BACKPACK_BE.get(), SimpleBlockEntityRenderer::new);
    }
}
