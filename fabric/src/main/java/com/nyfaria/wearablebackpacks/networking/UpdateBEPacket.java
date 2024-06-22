package com.nyfaria.wearablebackpacks.networking;

import com.nyfaria.wearablebackpacks.Constants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record UpdateBEPacket(BlockPos pos, int color) implements FabricPacket  {
    public static PacketType<UpdateBEPacket> TYPE = PacketType.create(new ResourceLocation(Constants.MODID, "update_be"), UpdateBEPacket::new);
    public UpdateBEPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt());
    }
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(color);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
