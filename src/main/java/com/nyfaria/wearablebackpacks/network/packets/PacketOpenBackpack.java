package com.nyfaria.wearablebackpacks.network.packets;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
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
        if(BackpackConfig.INSTANCE.canOpenWhileEquipped.get()) {
            Player player = context.getSender();
            if (player != null) {
                ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
                if (stack.is(ItemInit.BACKPACK.get())) {
                    NetworkHooks.openScreen((ServerPlayer) player, new BackpackItem.ContainerProvider(stack.getDisplayName(), BackpackItem.getInventory(stack), player, player), a -> {
                        a.writeNbt(BackpackItem.getInventory(stack).serializeNBT());
                    });
                }
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
    }
    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, PacketOpenBackpack.class, PacketOpenBackpack::read);
    }
}