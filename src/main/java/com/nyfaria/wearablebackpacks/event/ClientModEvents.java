package com.nyfaria.wearablebackpacks.event;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleArmorRenderer;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleBlockEntityRenderer;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ContainerInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.screens.BackpackBEContainerScreen;
import com.nyfaria.wearablebackpacks.screens.BackpackContainerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = WearableBackpacks.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    public static KeyBinding OPEN_BACKPACK;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ScreenManager.register(ContainerInit.BACKPACK_CONTAINER.get(), BackpackContainerScreen::new);
        ScreenManager.register(ContainerInit.BACKPACK_BE_CONTAINER.get(), BackpackBEContainerScreen::new);
        RenderTypeLookup.setRenderLayer(BlockInit.BACKPACK.get(), RenderType.cutout());
        GeoArmorRenderer.registerArmorRenderer(BackpackItem.class, SimpleArmorRenderer::new);
        OPEN_BACKPACK = new KeyBinding("keys.wearablebackpacks.open_backpack", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_B, "keys.category.wearablebackpacks");
        ClientRegistry.registerKeyBinding(OPEN_BACKPACK);
        ClientRegistry.bindTileEntityRenderer(BlockInit.BACKPACK_BE.get(), SimpleBlockEntityRenderer::new);
    }


    @SubscribeEvent
    public static void onBlockColor(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, level, pos, tintIndex) -> {
            TileEntity be = level.getBlockEntity(pos);
            if (be instanceof BackpackBlockEntity bbe) {
                return bbe.getColor();
            }
            return 0;
        }, BlockInit.BACKPACK.get());
    }
}
