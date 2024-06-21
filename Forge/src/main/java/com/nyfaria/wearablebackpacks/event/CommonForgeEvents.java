package com.nyfaria.wearablebackpacks.event;

import com.nyfaria.wearablebackpacks.CommonClass;
import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import com.nyfaria.wearablebackpacks.cap.WornBackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.platform.Services;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {


    @SubscribeEvent
    public static void attribs(PlayerInteractEvent.EntityInteract event) {
        if (!BackpackConfig.INSTANCE.canOpenOthers.get()) return;
        if (event.getEntity().level().isClientSide) return;
        if (event.getTarget() instanceof Player targetEntity) {
            Player player = event.getEntity();
            CommonClass.openOtherBackpack(player, targetEntity);
        }
    }
    @SubscribeEvent
    public static void onClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if(!event.getEntity().getMainHandItem().isEmpty())return;
        if (event.getEntity().isShiftKeyDown()) {
            if (event.getFace() == Direction.UP) {
                Player player = event.getEntity();
                BlockHitResult hitVec = event.getHitVec();
                CommonClass.setBackpack(player, hitVec);
            }
        }
    }


}
