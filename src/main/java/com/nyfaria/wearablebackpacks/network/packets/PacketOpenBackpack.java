package com.nyfaria.wearablebackpacks.network.packets;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketOpenBackpack implements IPacket {

    public PacketOpenBackpack() {
    }

    public static PacketOpenBackpack read(PacketBuffer buffer) {
        return new PacketOpenBackpack();
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.setPacketHandled(true);
        if(BackpackConfig.INSTANCE.canOpenWhileEquipped.get()) {
            PlayerEntity player = context.getSender();
            if (player != null) {
                ItemStack stack = player.getItemBySlot(EquipmentSlotType.CHEST);
                if (stack.getItem() == (ItemInit.BACKPACK.get())) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new BackpackItem.ContainerProvider(stack.getDisplayName(), BackpackItem.getInventory(stack), player, player), a -> {
                        a.writeNbt(BackpackItem.getInventory(stack).serializeNBT());
                    });
                }
            }
        }
    }

    @Override
    public void write(PacketBuffer friendlyByteBuf) {
    }
    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, PacketOpenBackpack.class, PacketOpenBackpack::read);
    }
}