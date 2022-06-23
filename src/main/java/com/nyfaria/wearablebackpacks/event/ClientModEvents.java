package com.nyfaria.wearablebackpacks.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleArmorRenderer;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleBlockEntityRenderer;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ContainerInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import com.nyfaria.wearablebackpacks.network.packets.PacketOpenBackpack;
import com.nyfaria.wearablebackpacks.screens.BackpackBEContainerScreen;
import com.nyfaria.wearablebackpacks.screens.BackpackContainerScreen;
import com.nyfaria.wearablebackpacks.tooltip.BackpackTooltip;
import com.nyfaria.wearablebackpacks.tooltip.ClientBackpackTooltip;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = WearableBackpacks.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    public static KeyMapping OPEN_BACKPACK;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerInit.BACKPACK_CONTAINER.get(), BackpackContainerScreen::new);
        MenuScreens.register(ContainerInit.BACKPACK_BE_CONTAINER.get(), BackpackBEContainerScreen::new);
        ItemBlockRenderTypes.setRenderLayer(BlockInit.BACKPACK.get(), RenderType.cutout());
        MinecraftForgeClient.registerTooltipComponentFactory(BackpackTooltip.class, ClientBackpackTooltip::new);
        GeoArmorRenderer.registerArmorRenderer(BackpackItem.class, SimpleArmorRenderer::new);
        OPEN_BACKPACK = new KeyMapping("keys.wearablebackpacks.open_backpack", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,InputConstants.KEY_B, "keys.category.wearablebackpacks");
        ClientRegistry.registerKeyBinding(OPEN_BACKPACK);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockInit.BACKPACK_BE.get(), SimpleBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onBlockColor(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, level, pos, tintIndex) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BackpackBlockEntity bbe) {
                return bbe.getColor();
            }
            return 0;
        }, BlockInit.BACKPACK.get());
    }
}
