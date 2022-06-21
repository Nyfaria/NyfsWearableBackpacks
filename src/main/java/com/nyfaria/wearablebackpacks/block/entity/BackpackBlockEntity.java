package com.nyfaria.wearablebackpacks.block.entity;

import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.cap.BackpackBEHolderAttacher;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BackpackBlockEntity extends SyncingBlockEntity implements IAnimatable {
    private final AnimationFactory animationFactory = new AnimationFactory(this);
    private boolean accessed = false;

    public BackpackBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockInit.BACKPACK_BE.get(), pWorldPosition, pBlockState);
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
    public static BackpackInventory getInventory(BackpackBlockEntity itemStack) {
        return BackpackBEHolderAttacher.getBackpackBEHolderUnwrap(itemStack).getInventory();
    }
    public void openMenu(Player pPlayer){
        NetworkHooks.openGui((ServerPlayer) pPlayer, new BackpackItem.ContainerProvider(new TextComponent("Backpack"), getInventory(this),pPlayer,pPlayer), a -> {
            a.writeNbt(getInventory(this).serializeNBT());
        });
    }
    @Override
    public void saveData(CompoundTag pTag) {
        pTag.putBoolean("accessed", accessed);
         BackpackBEHolderAttacher.getBackpackBEHolder(this).ifPresent(cap -> pTag.put("inventory",cap.serializeNBT(true)));
    }

    @Override
    public void loadData(CompoundTag pTag) {
        accessed = pTag.getBoolean("accessed");
        BackpackBEHolderAttacher.getBackpackBEHolder(this).ifPresent(cap -> cap.deserializeNBT(pTag.getCompound("inventory"), true));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    public void setAccessed(boolean accessed) {
        this.accessed = accessed;
    }
}
