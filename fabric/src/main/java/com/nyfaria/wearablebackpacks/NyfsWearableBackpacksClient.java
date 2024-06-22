package com.nyfaria.wearablebackpacks;

import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.client.CommonClientClass;
import com.nyfaria.wearablebackpacks.client.layer.BackPackRenderLayer;
import com.nyfaria.wearablebackpacks.client.screen.BackpackContainerScreen;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.init.MenuInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.networking.UpdateBEPacket;
import com.nyfaria.wearablebackpacks.tooltip.BackpackTooltip;
import com.nyfaria.wearablebackpacks.tooltip.ClientBackpackTooltip;
import com.nyfaria.wearablebackpacks.util.ClientNetworkHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NyfsWearableBackpacksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(MenuInit.BACKPACK_MENU.get(), BackpackContainerScreen::new);
        MenuScreens.register(MenuInit.BACKPACK_BE_MENU.get(), BackpackContainerScreen::new);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (stack.getItem() instanceof BackpackItem) {
                return ((BackpackItem) stack.getItem()).getColor(stack);
            }
            return 0;
        }, ItemInit.BACKPACK.get());
        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BackpackBlockEntity bbe) {
                return bbe.getColor();
            }
            return -1;
        }, BlockInit.BACKPACK.get());
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            while (CommonClientClass.OPEN_BACKPACK.consumeClick()) {
                ClientPlayNetworking.send(new ResourceLocation(Constants.MODID, "open_backpack"),
                        new FriendlyByteBuf(Unpooled.buffer()));
            }
        });


        TooltipComponentCallback.EVENT.register((component) -> {
            if (component instanceof BackpackTooltip) {
                return new ClientBackpackTooltip((BackpackTooltip) component);
            }
            return null;
        });
        ClientPlayNetworking.registerGlobalReceiver(UpdateBEPacket.TYPE, (packet, player, packetSender) -> {

            BlockPos pos = packet.pos();
            int color = packet.color();
            ClientNetworkHandler.getLevel().setBlock(pos, BlockInit.BACKPACK.get().defaultBlockState(), 11);
            BackpackBlockEntity be = (BackpackBlockEntity) ClientNetworkHandler.getLevel().getBlockEntity(pos);
            if (be != null) {
                be.setColor(color);
            }
        });

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, renderer, registrationHelper, context) -> {
                    if (CommonClientClass.backpackHavers.contains(entityType)) {
                        registrationHelper.register(new BackPackRenderLayer<>((RenderLayerParent) renderer));
                    }
                }
        );
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.BACKPACK.get(), RenderType.cutout());
    }
}
