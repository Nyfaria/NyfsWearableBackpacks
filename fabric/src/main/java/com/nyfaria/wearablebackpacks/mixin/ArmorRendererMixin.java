package com.nyfaria.wearablebackpacks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class ArmorRendererMixin {

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource buffer, LivingEntity livingEntity, EquipmentSlot slot, int packedLight, HumanoidModel model, CallbackInfo ci)  {
        if(livingEntity.getItemBySlot(slot).is(ItemInit.BACKPACK.get())){
            ci.cancel();
        }
        // Insert your code here


    }
}
