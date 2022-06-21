package com.nyfaria.wearablebackpacks.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class BackpackTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;
    private final int weight;

    public BackpackTooltip(NonNullList<ItemStack> p_150677_, int p_150678_) {
        this.items = p_150677_;
        this.weight = p_150678_;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public int getWeight() {
        return this.weight;
    }

}
