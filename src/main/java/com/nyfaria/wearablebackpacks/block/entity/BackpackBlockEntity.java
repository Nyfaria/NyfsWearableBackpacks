package com.nyfaria.wearablebackpacks.block.entity;

import com.nyfaria.wearablebackpacks.backpack.BackpackBEContainer;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class BackpackBlockEntity extends LockableTileEntity implements IAnimatable {
    private final AnimationFactory animationFactory = new AnimationFactory(this);
    private boolean accessed = false;
    private int color = 0;

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        load(getBlockState(),pkt.getTag());
    }

    private NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
    private CompoundNBT backpackTag;

    public BackpackBlockEntity() {
        super(BlockInit.BACKPACK_BE.get());
    }

    public void setBackpackTag(CompoundNBT tag) {
        this.backpackTag = tag;
        updateBlock();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        AnimationController controller = event.getController();
        if (accessed) {
            controller.setAnimation(new AnimationBuilder().addAnimation("open", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public static NonNullList<ItemStack> getInventory(BackpackBlockEntity itemStack) {
        return itemStack.items;
    }

    @Nullable
    @Override
    public Container createMenu(int pContainerId, PlayerInventory pInventory, PlayerEntity pPlayer) {
        return super.createMenu(pContainerId, pInventory, pPlayer);
    }


    public void load(BlockState blockState, CompoundNBT pTag) {
        super.load(blockState, pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(pTag, this.items);
        this.accessed = pTag.getBoolean("accessed");
        this.color = pTag.getInt("color");
        this.backpackTag = pTag.getCompound("backpackTag");
    }

    @Override
    public CompoundNBT save(CompoundNBT pTag) {
        ItemStackHelper.saveAllItems(pTag, this.items);
        pTag.putBoolean("accessed", accessed);
        pTag.putInt("color", color);
        pTag.put("backpackTag", backpackTag);
        return super.save(pTag);
    }


    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    public void setAccessed(boolean accessed) {
        this.accessed = accessed;
    }

    @Override
    public int getContainerSize() {
        return 36;
    }

    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.accessed = nbt.getBoolean("accessed");
        this.color = nbt.getInt("color");
        this.backpackTag = nbt.getCompound("backpackTag");
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        ItemStackHelper.saveAllItems(nbt, this.items);
        nbt.putBoolean("accessed", accessed);
        nbt.putInt("color", color);
        nbt.put("backpackTag", backpackTag);
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        return save(tag);
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack itemstack = ItemStackHelper.removeItem(this.items, pIndex, pCount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ItemStackHelper.takeItem(items, pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        items.set(pIndex, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }


    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    public void setColor(int color) {
        this.color = color;
        updateBlock();
    }

    public int getColor() {
        return color;
    }

    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
        updateBlock();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("Backpack");
    }

    @Override
    protected Container createMenu(int pContainerId, PlayerInventory pInventory) {
        return new BackpackBEContainer(pContainerId, pInventory, this, getBlockPos());
    }

    @Override
    public void clearContent() {

    }

    public void updateBlock() {
        BlockState blockState = getBlockState();
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 0, getUpdateTag());
    }

    public CompoundNBT getBackpackTag() {
        return backpackTag;
    }
}
