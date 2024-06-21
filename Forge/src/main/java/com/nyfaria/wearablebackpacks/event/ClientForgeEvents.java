package com.nyfaria.wearablebackpacks.event;

import com.nyfaria.wearablebackpacks.client.CommonClientClass;
import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import com.nyfaria.wearablebackpacks.network.PacketOpenBackpack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onKey(TickEvent.ClientTickEvent e) {
        while(CommonClientClass.OPEN_BACKPACK.consumeClick()) {
            NetworkHandler.INSTANCE.sendToServer(new PacketOpenBackpack());
        }
    }
}
