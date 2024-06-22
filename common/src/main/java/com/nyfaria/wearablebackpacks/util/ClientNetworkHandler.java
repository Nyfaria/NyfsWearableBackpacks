package com.nyfaria.wearablebackpacks.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ClientNetworkHandler {
    public static Level getLevel() {
        return Minecraft.getInstance().level;
    }
}
