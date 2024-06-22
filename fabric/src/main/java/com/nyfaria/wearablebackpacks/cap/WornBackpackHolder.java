package com.nyfaria.wearablebackpacks.cap;

import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WornBackpackHolder implements ComponentV3, Component, CopyableComponent<WornBackpackHolder>, AutoSyncedComponent, BackpackHolder {
    private ItemStack backpackStack = ItemStack.EMPTY;

    public WornBackpackHolder(LivingEntity player) {
    }

    @Override
    public ItemStack getBackpackStack() {
        return backpackStack;
    }


    @Override
    public void setBackpackStack(ItemStack stack) {
        backpackStack = stack;
        BackpackHolderAttacher.WORN_BACKPACK.sync(this);
    }
    @Override
    public void copyFrom(WornBackpackHolder other) {
        this.backpackStack = other.backpackStack;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        backpackStack = ItemStack.of(tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        backpackStack.save(tag);
    }


}
