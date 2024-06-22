package com.nyfaria.wearablebackpacks.network;

import com.nyfaria.wearablebackpacks.CommonClass;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.util.ClientNetworkHandler;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record PacketUpdateBE(BlockPos pos, int color) implements IPacket {



    public static PacketUpdateBE read(FriendlyByteBuf buffer) {
        return new PacketUpdateBE(buffer.readBlockPos(),buffer.readInt());
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.setPacketHandled(true);
        ClientNetworkHandler.getLevel().setBlock(pos, BlockInit.BACKPACK.get().defaultBlockState(), 11);
        BackpackBlockEntity be = (BackpackBlockEntity) ClientNetworkHandler.getLevel().getBlockEntity(pos);
        if(be != null) {
            be.setColor(color);
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(pos);
        friendlyByteBuf.writeInt(color);
    }
    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, PacketUpdateBE.class, PacketUpdateBE::read);
    }
}