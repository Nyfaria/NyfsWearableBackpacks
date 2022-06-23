package com.nyfaria.wearablebackpacks.network;

import com.google.common.collect.ImmutableList;
import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.network.packets.PacketOpenBackpack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.6";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(WearableBackpacks.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;

    public static void register() {
        List<BiConsumer<SimpleChannel, Integer>> packets = ImmutableList.<BiConsumer<SimpleChannel, Integer>>builder()
                .add(PacketOpenBackpack::register)
                .build();

        packets.forEach(consumer -> consumer.accept(INSTANCE, getNextId()));
    }

    private static int getNextId() {
        return nextId++;
    }
}