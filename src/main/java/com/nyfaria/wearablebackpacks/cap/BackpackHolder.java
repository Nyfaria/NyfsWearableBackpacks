package com.nyfaria.wearablebackpacks.cap;

import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import dev._100media.capabilitysyncer.core.ItemStackCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class BackpackHolder extends ItemStackCapability {
    private final BackpackInventory inventory;

    public BackpackHolder(ItemStack itemStack) {
        super(itemStack);
        this.inventory = new BackpackInventory(false);
    }


    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.put("inventory", inventory.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt == null) return;
        if (nbt.contains("inventory")) {
            inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
    }

    public BackpackInventory getInventory() {
        return inventory;
    }

}
