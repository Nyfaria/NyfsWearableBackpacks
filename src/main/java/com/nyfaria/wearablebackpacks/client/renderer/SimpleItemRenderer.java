package com.nyfaria.wearablebackpacks.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nyfaria.wearablebackpacks.client.model.SimpleModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SimpleItemRenderer<T extends Item & IAnimatable & DyeableLeatherItem> extends GeoItemRenderer<T> {

    T item;
    public SimpleItemRenderer() {
        super(new SimpleModel<>("block"));
    }

    @Override
    public void render(T animatable, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, ItemStack itemStack) {
        item = animatable;
        super.render(animatable, stack, bufferIn, packedLightIn, itemStack);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(bone.getName().contains("color")) {
            Color color = Color.ofOpaque(item.getColor(currentItemStack));
            float[] colors = new float[]{color.getRed(), color.getGreen(),color.getBlue()};
            super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, colors[0], colors[1], colors[2], alpha);
        }else{
            super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}
