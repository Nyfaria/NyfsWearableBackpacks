package com.nyfaria.wearablebackpacks.cap;

import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.EntityCapability;
import dev._100media.capabilitysyncer.core.PlayerCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.simple.SimpleChannel;

public class WornBackpackHolder extends EntityCapability {
    private ItemStack backpack = ItemStack.EMPTY;

    protected WornBackpackHolder(Entity entity) {
        super(entity);
    }

    public ItemStack getBackpack() {
        return backpack;
    }

    public void setBackpack(ItemStack backpack) {
        this.backpack = backpack;
        updateTracking();
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        backpack.save(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.backpack = ItemStack.of(nbt);
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.entity.getId(), WornBackpackHolderAttacher.RESOURCE_LOCATION, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }
}
