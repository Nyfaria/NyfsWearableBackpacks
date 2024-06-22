package com.nyfaria.wearablebackpacks;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.fml.config.ModConfig;

public class NyfsWearableBackpacks implements ModInitializer {

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.COMMON, BackpackConfig.CONFIG_SPEC);
        CommonClass.init();
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (hand == InteractionHand.MAIN_HAND) {
                if (player.isCrouching()) {
                    if (hitResult.getDirection() == Direction.UP) {
                        return CommonClass.setBackpack(player, hitResult);
                    }
                }
            }
            return InteractionResult.PASS;
        });
        ServerPlayNetworking.registerGlobalReceiver(new ResourceLocation(Constants.MODID, "open_backpack"),
                (server, player, handler, buf, responseSender) -> {
                    server.execute(() -> {
                        CommonClass.openBackpack(player);
                    });
                });
//        ServerPlayerEvents.ALLOW_DEATH.register((player, damageSource, damageAmount) -> {
//            CommonClass.dropBackpack(player);
//            return true;
//        });
    }
}
