package com.nyfaria.wearablebackpacks.event;

import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import com.nyfaria.wearablebackpacks.network.packets.PacketOpenBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.IReverseTag;
import net.minecraftforge.registries.tags.ITagManager;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onKey(InputEvent.Key e) {
        if (ClientModEvents.OPEN_BACKPACK != null && ClientModEvents.OPEN_BACKPACK.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS && ClientModEvents.OPEN_BACKPACK.isConflictContextAndModifierActive()))
            NetworkHandler.INSTANCE.sendToServer(new PacketOpenBackpack());
    }
}
