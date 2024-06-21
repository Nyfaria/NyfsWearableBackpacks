package com.nyfaria.wearablebackpacks.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

public class CommonClientClass {
    public static KeyMapping OPEN_BACKPACK = new KeyMapping("key.wearablebackpacks.open", InputConstants.KEY_B, "key.categories.wearablebackpacks");
    public static void init() {
        registerKeyBindings();
    }

    public static void registerKeyBindings(){
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings,OPEN_BACKPACK);
    }
}
