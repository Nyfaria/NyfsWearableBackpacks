package com.nyfaria.wearablebackpacks.backpack;

import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ContainerInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.util.Dimension;
import com.nyfaria.wearablebackpacks.util.Point;
import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BackpackContainer extends Container {

    public final int rows;
    public final int columns;
    private final int padding = 8;
    private final int titleSpace = 10;
    private final boolean isItem;
    private final BlockPos pos;
    private final int ownerId;

    public BackpackContainer(int id, PlayerInventory player, BackpackInventory backpackInventory, BlockPos pos, boolean isItem, int ownerId) {
        super(ContainerInit.BACKPACK_CONTAINER.get(), id);
        this.rows = BackpackConfig.INSTANCE.rows.get();
        this.columns = BackpackConfig.INSTANCE.columns.get();
        this.isItem = isItem;
        this.pos = pos;
        this.ownerId = ownerId;
        this.addSlots(this.rows, this.columns, backpackInventory, player);

        ((LivingEntity) player.player.level.getEntity(ownerId)).getItemBySlot(EquipmentSlotType.CHEST).getOrCreateTag().putBoolean("accessed", true);

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
    private void addSlots(int rows, int columns, IItemHandler inventory, PlayerInventory player) {
        Dimension dimension = getDimension();
        int startX = 8;
        int startY = rows < 9 ? 17 : 8;

        rows = Math.min(rows, 9);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Point getBackpackSlotPosition = getBackpackSlotPosition(dimension, column, row);
                int index = row * columns + column;
                this.addSlot(new SlotItemHandler(inventory, index, getBackpackSlotPosition.x, getBackpackSlotPosition.y));
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
        return isItem || playerIn.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 2;
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
        if (isItem) {
            ItemStack chest = ((LivingEntity) pPlayer.level.getEntity(ownerId)).getItemBySlot(EquipmentSlotType.CHEST);
            ItemStack hand = ((LivingEntity) pPlayer.level.getEntity(ownerId)).getItemBySlot(EquipmentSlotType.MAINHAND);
            if (chest.getItem() == ItemInit.BACKPACK.get()) {
                chest.getOrCreateTag().putBoolean("accessed", false);
            } else if (hand.getItem() == ItemInit.BACKPACK.get()) {
                hand.getOrCreateTag().putBoolean("accessed", false);
            } else {
                ((BackpackBlockEntity) pPlayer.level.getBlockEntity(pos)).setAccessed(true);
            }

        } else {
            TileEntity blockEntity = pPlayer.level.getBlockEntity(pos);
            if (blockEntity != null) {
                ((BackpackBlockEntity) blockEntity).setAccessed(true);
                ((BackpackBlockEntity) blockEntity).updateBlock();
            }
        }
        super.removed(pPlayer);
    }

}
