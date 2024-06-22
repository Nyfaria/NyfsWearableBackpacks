package com.nyfaria.wearablebackpacks.network;

import com.nyfaria.wearablebackpacks.CommonClass;
import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketOpenBackpack implements IPacket {

    public PacketOpenBackpack() {
    }

    public static PacketOpenBackpack read(FriendlyByteBuf buffer) {
        return new PacketOpenBackpack();
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.setPacketHandled(true);
        CommonClass.openBackpack(context.getSender());
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
    }
    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, PacketOpenBackpack.class, PacketOpenBackpack::read);
    }
}