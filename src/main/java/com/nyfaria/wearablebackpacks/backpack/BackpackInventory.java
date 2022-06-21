package com.nyfaria.wearablebackpacks.backpack;

import com.nyfaria.wearablebackpacks.init.TagInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BackpackInventory extends ItemStackHandler {

    private final boolean remote;
    public int rows;
    public int columns;
    private final NonNullList<ItemStack> stacks;

    public BackpackInventory(boolean remote) {
        this.remote = remote;
        this.rows = 3;
        this.columns = 9;
        stacks = NonNullList.withSize(this.rows * this.columns, ItemStack.EMPTY);
    }

    private static boolean canStack(ItemStack stack1, ItemStack stack2) {
        return stack1.isEmpty() || stack2.isEmpty() || (stack1.getItem() == stack2.getItem() && stack1.getDamageValue() == stack2.getDamageValue() && ItemStack.tagMatches(stack1, stack2));
    }

    @Override
    public int getSlots() {
        return this.rows * this.columns;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.stacks.get(slot);
    }

    @Nonnull
    public ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        for(int x = 0; x < getSlots(); x++){
            if(getStackInSlot(x).is(stack.getItem()) && getStackInSlot(x).getCount() < 64){
                return insertItem(x, stack, simulate);
            }
        }
        return stack;
    }
    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ItemStack current = this.stacks.get(slot);
        if (!stack.isEmpty() && this.isItemValid(slot, stack) && canStack(current, stack)) {
            int amount = Math.min(stack.getCount(), 64 - current.getCount());
            if (!simulate) {
                ItemStack newStack = stack.copy();
                newStack.setCount(current.getCount() + amount);
                this.stacks.set(slot, newStack);
            }
            ItemStack result = stack.copy();
            result.shrink(amount);
            return result;
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack stack = this.stacks.get(slot);
        int count = Math.min(amount, stack.getCount());
        ItemStack result = stack.copy();
        if (!simulate) {
            stack.shrink(count);
            if (!this.remote && result.getItem() instanceof BackpackItem && result.getOrCreateTag().contains("invIndex")) {
                int index = result.getOrCreateTag().getInt("invIndex");
                boolean contains = false;
                for (ItemStack stack1 : this.stacks) {
                    if (stack1.getItem() instanceof BackpackItem && stack1.getOrCreateTag().contains("invIndex")
                            && stack1.getOrCreateTag().getInt("invIndex") == index) {
                        contains = true;
                        break;
                    }
                }
            }
        }
        result.setCount(count);
        return result;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return !stack.is(TagInit.BLACKLIST);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        ItemStack oldStack = this.stacks.get(slot);
        this.stacks.set(slot, stack);
    }

    public void adjustSize(int rows, int columns) {
        if (this.rows == rows && this.columns == columns)
            return;
        this.rows = rows;
        this.columns = columns;
        while (this.stacks.size() < this.rows * this.columns)
            this.stacks.add(ItemStack.EMPTY);
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        for (int slot = 0; slot < this.stacks.size(); slot++) {
            compound.put("stack" + slot, this.stacks.get(slot).save(new CompoundTag()));
        }
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        if (compound == null) return;
        this.rows = compound.contains("rows") ? compound.getInt("rows") : compound.getInt("slots") / 9; // Do this for compatibility with older versions
        this.columns = compound.contains("columns") ? compound.getInt("columns") : compound.getInt("slots") / 9; // Do this for compatibility with older versions
        this.stacks.clear();
        int size = compound.contains("stacks") ? compound.getInt("stacks") : this.rows * this.columns; // Do this for compatibility with older versions

        for (int slot = 0; slot < size; slot++) {
            this.stacks.set(slot, ItemStack.of(compound.getCompound("stack" + slot)));
        }

    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
    }

}
