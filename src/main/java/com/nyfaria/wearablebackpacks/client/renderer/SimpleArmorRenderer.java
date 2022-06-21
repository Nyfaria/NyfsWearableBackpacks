package com.nyfaria.wearablebackpacks.client.renderer;

import com.nyfaria.wearablebackpacks.client.model.SimpleModel;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SimpleArmorRenderer<T extends GeoArmorItem & IAnimatable> extends GeoArmorRenderer<T> {

    public SimpleArmorRenderer() {
        super(new SimpleModel<>("armor"));
    }
}
