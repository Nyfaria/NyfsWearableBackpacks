package com.nyfaria.wearablebackpacks.backpack;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ContainerInit;
import com.nyfaria.wearablebackpacks.util.Dimension;
import com.nyfaria.wearablebackpacks.util.Point;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class BackpackBEContainer extends Container {

    public final int rows;
    public final int columns;
    private final int padding = 8;
    private final int titleSpace = 10;
    private final BlockPos pos;
    private final IInventory container;

    public BackpackBEContainer(int id, PlayerInventory player) {
        this(id, player, new Inventory(BackpackConfig.INSTANCE.rows.get() * BackpackConfig.INSTANCE.columns.get()),BlockPos.ZERO);
    }
    public BackpackBEContainer(int id, PlayerInventory player, IInventory backpackInventory, BlockPos pos) {
        super(ContainerInit.BACKPACK_BE_CONTAINER.get(), id);
        this.rows = BackpackConfig.INSTANCE.rows.get();
        this.columns = BackpackConfig.INSTANCE.columns.get();
        this.pos = pos;
        container = backpackInventory;
        this.addSlots(this.rows, this.columns, backpackInventory, player);
        container.startOpen(player.player);

    }

    public Dimension getDimension() {
        return new Dimension(padding * 2 + Math.max(this.columns, 9) * 18, padding * 2 + titleSpace * 2 + 8 + (this.rows + 4) * 18);
    }

    public Point getBackpackSlotPosition(Dimension dimension, int x, int y) {
        return new Point(dimension.getWidth() / 2 - columns * 9 + x * 18, padding + titleSpace + y * 18);
    }

    public Point getPlayerInvSlotPosition(Dimension dimension, int x, int y) {

        return new Point(dimension.getWidth() / 2 - 9 * 9 + x * 18, dimension.getHeight() - padding - 4 * 18 - 3 + y * 18 + (y == 3 ? 4 : 0));

    }

    @SuppressWarnings("unused")
    private void addSlots(int rows, int columns, IInventory inventory, PlayerInventory player) {
        Dimension dimension = getDimension();
        int startX = 8;
        int startY = rows < 9 ? 17 : 8;

        rows = Math.min(rows, 9);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Point getBackpackSlotPosition = getBackpackSlotPosition(dimension, column, row);
                int index = row * columns + column;
                this.addSlot(new BackpackSlot(inventory, index, getBackpackSlotPosition.x, getBackpackSlotPosition.y));
            }
        }

        startX = 8 + (columns - 9) * 9;
        startY += rows * 18 + (rows == 9 ? 4 : 13);

        // player slots
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, column, row);
                int index = column + row * 9 + 9;
                this.addSlot(new Slot(player, index, playerInvSlotPosition.x, playerInvSlotPosition.y));
            }
        }

        startY += 58;

        for (int column = 0; column < 9; column++) {
            Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, column, 3);
            this.addSlot(new Slot(player, column, playerInvSlotPosition.x, playerInvSlotPosition.y));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return playerIn.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack returnStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            returnStack = slotStack.copy();
            if (index < this.rows * this.columns) {
                if (!this.moveItemStackTo(slotStack, this.rows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return returnStack;
    }


    @Override
    public void removed(PlayerEntity pPlayer) {
        container.stopOpen(pPlayer);
        super.removed(pPlayer);
    }

//    @Override
//    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
//        if (clickTypeIn == ClickType.PICKUP && dragType == 1 && slotId >= 0) {
//            Slot slot = this.getSlot(slotId);
//            if (slot.mayPickup(player)) {
//                ItemStack stack = slot.getItem();
//                if (stack.getItem() instanceof BackpackItem) {
//                    if (!player.level.isClientSide) {
//                        int bagSlot = slotId >= (this.rows + 3) * 9 ? slotId - (this.rows + 3) * 9 : slotId >= this.rows * 9 ? slotId - (this.rows - 1) * 9 : -1;
//                    }
//                    return;
//                }
//            }
//        }
//        super.clicked(slotId, dragType, clickTypeIn, player);
//    }
}
