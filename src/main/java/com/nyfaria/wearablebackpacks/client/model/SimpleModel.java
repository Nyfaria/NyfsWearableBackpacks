package com.nyfaria.wearablebackpacks.client.model;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SimpleModel<T extends IAnimatable> extends AnimatedGeoModel<T> {
    private final ResourceLocation texture;
    private final ResourceLocation model;
    private final ResourceLocation animations;

    public SimpleModel(String folder) {
        this.texture = new ResourceLocation(WearableBackpacks.MODID, "textures/" + folder + "/backpack.png");
        this.model = new ResourceLocation(WearableBackpacks.MODID, "geo/" + folder + "/backpack.geo.json");
        this.animations = new ResourceLocation(WearableBackpacks.MODID, "animations/" + folder + "/backpack.animation.json");
    }
    @Override
    public ResourceLocation getModelLocation(T object) {
        return model;
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return animations;
    }
}
