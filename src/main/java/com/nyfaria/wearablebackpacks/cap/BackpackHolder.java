package com.nyfaria.wearablebackpacks.cap;

import com.google.common.collect.Lists;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import dev._100media.capabilitysyncer.core.ItemStackCapability;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class BackpackHolder extends ItemStackCapability {
    private final BackpackInventory inventory;

    public BackpackHolder(ItemStack itemStack) {
        super(itemStack);
        this.inventory = new BackpackInventory(false);
    }


    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag,inventory.getStacks());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt == null) return;
        NonNullList<ItemStack> stacks = NonNullList.withSize(BackpackConfig.INSTANCE.rows.get() * BackpackConfig.INSTANCE.columns.get(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt,stacks);
        inventory.setStacks(stacks);
    }

    public BackpackInventory getInventory() {
        return inventory;
    }

    public void fill(LootContext pContext, LootTable pTable) {
        ObjectArrayList<ItemStack> list = pTable.getRandomItems(pContext);
        RandomSource random = pContext.getRandom();
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
    private List<Integer> getAvailableSlots(BackpackInventory pInventory, RandomSource pRand) {
        ObjectArrayList<Integer> list = new ObjectArrayList();

        for(int i = 0; i < pInventory.getStacks().size(); ++i) {
            if (pInventory.getStackInSlot(i).isEmpty()) {
                list.add(i);
            }
        }

        Util.shuffle(list, pRand);
        return list;
    }
    private void shuffleAndSplitItems(ObjectArrayList<ItemStack> pStacks, int pEmptySlotsCount, RandomSource pRand) {
        ObjectArrayList<ItemStack> list = new ObjectArrayList();
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
            ItemStack itemstack2 = list.remove(Mth.nextInt(pRand, 0, list.size() - 1));
            int i = Mth.nextInt(pRand, 1, itemstack2.getCount() / 2);
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
        Util.shuffle(pStacks, pRand);
    }
}
