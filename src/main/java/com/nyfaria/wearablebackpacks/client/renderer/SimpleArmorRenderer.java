package com.nyfaria.wearablebackpacks.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nyfaria.wearablebackpacks.client.model.SimpleModel;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SimpleArmorRenderer<T extends BackpackItem & IAnimatable> extends GeoArmorRenderer<T> {

    public SimpleArmorRenderer() {
        super(new SimpleModel<>("armor"));
    }

    @Override
    public void render(float partialTicks, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn) {
        super.render(partialTicks, stack, bufferIn, packedLightIn);
    }



    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(bone.getName().contains("color")) {
            int i = currentArmorItem.getColor(itemStack);
            float r = (float) (i >> 16 & 255) / 255.0F;
            float g = (float) (i >> 8 & 255) / 255.0F;
            float b = (float) (i & 255) / 255.0F;
            super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, r, g, b, alpha);
        }else{
            super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}
