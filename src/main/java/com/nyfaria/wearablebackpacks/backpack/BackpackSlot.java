package com.nyfaria.wearablebackpacks.backpack;

import com.nyfaria.wearablebackpacks.init.TagInit;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class BackpackSlot extends Slot {

    public BackpackSlot(IInventory p_39521_, int p_39522_, int p_39523_, int p_39524_) {
        super(p_39521_, p_39522_, p_39523_, p_39524_);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack pStack) {
        return !pStack.getItem().is(TagInit.BLACKLIST);
    }


}
