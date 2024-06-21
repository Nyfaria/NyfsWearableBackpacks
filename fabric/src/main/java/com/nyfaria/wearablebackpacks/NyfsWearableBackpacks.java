package com.nyfaria.wearablebackpacks;

import com.nyfaria.wearablebackpacks.init.EntityInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.CommonClass;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;

public class NyfsWearableBackpacks implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
        UseBlockCallback.EVENT.register((player, world, hand, hitResult)->{
            if(!world.isClientSide) {
                if (hand == InteractionHand.MAIN_HAND) {
                    if (player.isCrouching()) {
                        if (hitResult.getDirection() == Direction.UP) {
                            if (player.getItemBySlot(EquipmentSlot.CHEST).is(ItemInit.BACKPACK.get())) {
                                return ItemInit.BACKPACK_ITEM.place(new ItemPlacementContext(player, hand, player.getEquippedStack(EquipmentSlot.CHEST), hitResult));
                            }
                        }
                    }
                }
            }
            return InteractionResult.PASS;
        });
    }
}
