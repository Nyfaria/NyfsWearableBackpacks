package com.nyfaria.wearablebackpacks.backpack;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.TagInit;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BackpackContainer implements Container {

    private ItemStack backpackStack;
    private NonNullList<ItemStack> items;
    public BackpackContainer(ItemStack backpackStack) {
        this.backpackStack = backpackStack;
//        this.items = contents.get(DataComponentInit.QUIVER_CONTENTS.get()).items();
        this.items = NonNullList.withSize(getColumns() * getRows(),ItemStack.EMPTY);
        ContainerHelper.loadAllItems(backpackStack.getTag().getCompound("Items"), items);
    }
    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
//        return contents.get(DataComponentInit.QUIVER_CONTENTS.get()).isEmpty();
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack stack = items.get(pSlot);
        ItemStack newStack = stack.split(pAmount);
        if (stack.isEmpty()) {
            items.set(pSlot, ItemStack.EMPTY);
        }
        setChanged();
        return newStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack stack = items.get(pSlot);
        items.set(pSlot, ItemStack.EMPTY);
        return stack;
    }

    public ItemStack addItem(ItemStack stack){
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack);
                setChanged();
                return ItemStack.EMPTY;
            } else {
                ItemStack itemStack = items.get(i);
                if (ItemStack.isSameItemSameTags(stack, itemStack)) {
                    int j = 64 - itemStack.getCount();
                    if (stack.getCount() <= j) {
                        ItemStack itemStack2 = itemStack.copy();
                        itemStack2.grow(stack.getCount());
                        items.set(i, itemStack2);
                        setChanged();
                        return ItemStack.EMPTY;
                    } else {
                        itemStack.grow(j);
                        stack.shrink(j);
                        setChanged();
                    }
                }

            }
        }
        return stack;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        items.set(pSlot, pStack);
        setChanged();
    }

    @Override
    public void setChanged() {
//        contents.set(DataComponentInit.QUIVER_CONTENTS.get(), QuiverContainerContents.fromItems(items));
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, items);
        backpackStack.getOrCreateTag().put("Items", tag);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int pSlot, ItemStack pStack) {
        return !pStack.is(TagInit.BLACKLIST);
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    public int getColumns() {
        return BackpackConfig.INSTANCE.columns.get();
    }
    public int getRows() {
        return BackpackConfig.INSTANCE.rows.get();
    }

    public NonNullList<ItemStack> getStacks() {
        return items;
    }
}
