package com.nyfaria.wearablebackpacks.block.entity;

import com.nyfaria.wearablebackpacks.backpack.BackpackBEContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.cap.BackpackBEHolderAttacher;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BackpackBlockEntity extends BaseContainerBlockEntity implements IAnimatable {
    private final AnimationFactory animationFactory = new AnimationFactory(this);
    private boolean accessed = false;
    private int color = 0;

    private NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
    private CompoundTag backpackTag;

    public BackpackBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockInit.BACKPACK_BE.get(), pWorldPosition, pBlockState);
    }

    public void setBackpackTag(CompoundTag tag) {
        this.backpackTag = tag;
        updateBlock();
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"controller", 0, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        AnimationController controller = event.getController();
        if(accessed) {
            controller.setAnimation(new AnimationBuilder().addAnimation("open", true));
            return PlayState.CONTINUE;
        }
        return  PlayState.STOP;
    }
    public static NonNullList<ItemStack> getInventory(BackpackBlockEntity itemStack) {
        return itemStack.items;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return super.createMenu(pContainerId, pInventory, pPlayer);
    }


    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);
        this.accessed = pTag.getBoolean("accessed");
        this.color = pTag.getInt("color");
        this.backpackTag = pTag.getCompound("backpackTag");
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, this.items);
        pTag.putBoolean("accessed", accessed);
        pTag.putInt("color",color);
        pTag.put("backpackTag", backpackTag);
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
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, pIndex, pCount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(items, pIndex);
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
    public boolean stillValid(Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
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
    protected Component getDefaultName() {
        return new TextComponent("Backpack");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new BackpackBEContainer(pContainerId, pInventory,this,getBlockPos());
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
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getBackpackTag() {
        return backpackTag;
    }
}
