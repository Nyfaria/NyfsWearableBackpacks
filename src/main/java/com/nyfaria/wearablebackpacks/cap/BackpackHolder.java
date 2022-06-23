package com.nyfaria.wearablebackpacks.cap;

import com.google.common.collect.Lists;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import dev._100media.capabilitysyncer.core.ItemStackCapability;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BackpackHolder extends ItemStackCapability {
    private final BackpackInventory inventory;

    public BackpackHolder(ItemStack itemStack) {
        super(itemStack);
        this.inventory = new BackpackInventory(false);
    }


    @Override
    public CompoundNBT serializeNBT(boolean savingToDisk) {
        CompoundNBT tag = new CompoundNBT();
        ItemStackHelper.saveAllItems(tag,inventory.getStacks());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt, boolean readingFromDisk) {
        if (nbt == null) return;
        NonNullList<ItemStack> stacks = NonNullList.withSize(BackpackConfig.INSTANCE.rows.get() * BackpackConfig.INSTANCE.columns.get(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt,stacks);
        inventory.setStacks(stacks);
    }

    public BackpackInventory getInventory() {
        return inventory;
    }

    public void fill(LootContext pContext, LootTable pTable) {
        List<ItemStack> list = pTable.getRandomItems(pContext);
        Random random = pContext.getRandom();
        List<Integer> list1 = getAvailableSlots(inventory, random);
        shuffleAndSplitItems(list, list1.size(), random);

        for(ItemStack itemstack : list) {
            if (list1.isEmpty()) {
                return;
            }

            if (itemstack.isEmpty()) {
                inventory.setStackInSlot(list1.remove(list1.size() - 1), ItemStack.EMPTY);
            } else {
                inventory.setStackInSlot(list1.remove(list1.size() - 1), itemstack);
            }
        }

    }
    private List<Integer> getAvailableSlots(BackpackInventory pInventory, Random pRand) {
        List<Integer> list = Lists.newArrayList();

        for(int i = 0; i < pInventory.getStacks().size(); ++i) {
            if (pInventory.getStackInSlot(i).isEmpty()) {
                list.add(i);
            }
        }

        Collections.shuffle(list, pRand);
        return list;
    }
    private void shuffleAndSplitItems(List<ItemStack> pStacks, int pEmptySlotsCount, Random pRand) {
        List<ItemStack> list = Lists.newArrayList();
        Iterator<ItemStack> iterator = pStacks.iterator();

        while(iterator.hasNext()) {
            ItemStack itemstack = iterator.next();
            if (itemstack.isEmpty()) {
                iterator.remove();
            } else if (itemstack.getCount() > 1) {
                list.add(itemstack);
                iterator.remove();
            }
        }

        while(pEmptySlotsCount - pStacks.size() - list.size() > 0 && !list.isEmpty()) {
            ItemStack itemstack2 = list.remove(MathHelper.nextInt(pRand, 0, list.size() - 1));
            int i = MathHelper.nextInt(pRand, 1, itemstack2.getCount() / 2);
            ItemStack itemstack1 = itemstack2.split(i);
            if (itemstack2.getCount() > 1 && pRand.nextBoolean()) {
                list.add(itemstack2);
            } else {
                pStacks.add(itemstack2);
            }

            if (itemstack1.getCount() > 1 && pRand.nextBoolean()) {
                list.add(itemstack1);
            } else {
                pStacks.add(itemstack1);
            }
        }

        pStacks.addAll(list);
        Collections.shuffle(pStacks, pRand);
    }
}
