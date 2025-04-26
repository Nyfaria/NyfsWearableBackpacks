package com.nyfaria.wearablebackpacks.backpack;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.platform.Services;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface BackpackHolder {

    ItemStack getBackpackStack();
    void setBackpackStack(ItemStack stack);

    static ItemStack getBackpackStack(LivingEntity player) {
        ItemStack stack = Services.PLATFORM.getBackpackHolder(player).getBackpackStack();;
        if(stack.isEmpty()) {
            stack = player.getItemBySlot(EquipmentSlot.CHEST);
        }
        if(stack.getItem() instanceof BackpackItem) {
            return stack;
        }
        return ItemStack.EMPTY;
    }
    static boolean canEquipBackpack(LivingEntity player) {
        return BackpackConfig.INSTANCE.useChestSlot.get() ? player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() : getBackpackStack(player).isEmpty();
    }
}
