package com.nyfaria.wearablebackpacks.event;

import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.client.CommonClientClass;
import com.nyfaria.wearablebackpacks.client.layer.BackPackRenderLayer;
import com.nyfaria.wearablebackpacks.client.model.CustomModel;
import com.nyfaria.wearablebackpacks.client.screen.BackpackContainerScreen;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.init.MenuInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.tooltip.BackpackTooltip;
import com.nyfaria.wearablebackpacks.tooltip.ClientBackpackTooltip;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(
                skin->event.getSkin(skin).addLayer(new BackPackRenderLayer(event.getSkin(skin)))
        );
        BuiltInRegistries.ENTITY_TYPE.forEach((type) -> {
            if(LivingEntity.class.isAssignableFrom(type.getBaseClass())) {
                LivingEntityRenderer renderer =
                event.getRenderer((EntityType<? extends LivingEntity>) type);
                renderer.addLayer(new BackPackRenderLayer<>(renderer));
            }
        });
    }
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
         event.registerLayerDefinition(CustomModel.LAYER_LOCATION,CustomModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent e) {
        MenuScreens.register(MenuInit.BACKPACK_MENU.get(), BackpackContainerScreen::new);
        MenuScreens.register(MenuInit.BACKPACK_BE_MENU.get(), BackpackContainerScreen::new);
        CommonClientClass.init();
    }
    @SubscribeEvent
    public static void registerTooltipComponentFactory(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(BackpackTooltip.class, ClientBackpackTooltip::new);
    }

    @SubscribeEvent
    public static void onBlockColor(RegisterColorHandlersEvent.Item event) {
        event.getItemColors().register((stack, tintIndex) -> {
            if (stack.getItem() instanceof BackpackItem) {
                return ((BackpackItem) stack.getItem()).getColor(stack);
            }
            return 0;
        }, ItemInit.BACKPACK.get());
    }
    @SubscribeEvent
    public static void onBlockColor(RegisterColorHandlersEvent.Block event) {
        event.getBlockColors().register((state, level, pos, tintIndex) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BackpackBlockEntity bbe) {
                return bbe.getColor();
            }
            return -1;
        }, BlockInit.BACKPACK.get());
    }
}
