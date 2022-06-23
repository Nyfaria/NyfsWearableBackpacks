package com.nyfaria.wearablebackpacks.mixin;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract int getSlotIndex();

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    public void  cantPickUpBackPack(Player pPlayer, CallbackInfoReturnable<Boolean> cir){
        if(getSlotIndex()  != 38) return;
        if(getItem().is(ItemInit.BACKPACK.get())){
            cir.setReturnValue(BackpackConfig.INSTANCE.canEquipFromInventory.get());
        }
    }
}
