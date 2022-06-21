package com.nyfaria.wearablebackpacks.network.packets;

import com.nyfaria.wearablebackpacks.item.BackpackItem;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketRename implements IPacket {

    private final String name;

    public PacketRename(String name) {
        this.name = name;
    }

    public static PacketRename read(FriendlyByteBuf buffer) {
        return new PacketRename(buffer.readBoolean() ? buffer.readUtf(32767) : null);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.setPacketHandled(true);

        Player player = context.getSender();
        if (player != null) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (stack.isEmpty() || !(stack.getItem() instanceof BackpackItem))
                stack = player.getItemInHand(InteractionHand.OFF_HAND);
            if (stack.isEmpty() || !(stack.getItem() instanceof BackpackItem))
                return;

            stack.setHoverName(new TextComponent(this.name));
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.name != null);
        if (this.name != null)
            friendlyByteBuf.writeUtf(this.name);
    }
    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, PacketRename.class, PacketRename::read);
    }
}