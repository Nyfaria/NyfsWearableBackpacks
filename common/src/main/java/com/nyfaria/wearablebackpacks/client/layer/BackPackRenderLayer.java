package com.nyfaria.wearablebackpacks.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;

public class BackPackRenderLayer<T extends LivingEntity> extends RenderLayer<T, EntityModel<T>> {


    public BackPackRenderLayer(RenderLayerParent<T, EntityModel<T>> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
//        ItemStack stack = CommonForgeEvents.getBackPackStack(pLivingEntity);
        if (BackpackHolder.getBackpackStack(pLivingEntity).isEmpty())
            return;
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(180));
        pPoseStack.translate(0, 0.6, -2 / 16f);
        if (pLivingEntity.isCrouching()) {
            EntityRenderer<? super LivingEntity> render =
                    Minecraft.getInstance().getEntityRenderDispatcher()
                            .getRenderer(pLivingEntity);

            if (render instanceof LivingEntityRenderer) {
                @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
                        livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
                EntityModel<LivingEntity> model = livingRenderer.getModel();

                if (model instanceof HumanoidModel) {
                    pPoseStack.mulPose(Axis.XN.rotation(((HumanoidModel<LivingEntity>) model).body.xRot));
                }
            }
            pPoseStack.translate(0.0F, -3 / 16f, -6 / 16f);
        }
        Minecraft.getInstance().getItemRenderer().renderStatic(BackpackHolder.getBackpackStack(pLivingEntity), ItemDisplayContext.HEAD, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer,null, 1);
        pPoseStack.popPose();
    }
}
