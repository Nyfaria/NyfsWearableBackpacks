package com.nyfaria.wearablebackpacks.backpack;

import com.nyfaria.wearablebackpacks.util.Dimension;
import com.nyfaria.wearablebackpacks.util.Point;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class BackpackInfoMenu extends AbstractContainerMenu {
    protected BackpackInfoMenu(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    public abstract Dimension getDimension();
    public abstract Point getPlayerInvSlotPosition(Dimension dimension, int x, int y);
}
