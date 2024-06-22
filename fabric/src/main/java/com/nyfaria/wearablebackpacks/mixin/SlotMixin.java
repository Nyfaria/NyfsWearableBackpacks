package com.nyfaria.wearablebackpacks.mixin;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets="net.minecraft.world.inventory.InventoryMenu$1")
public abstract class SlotMixin extends Slot {

    public SlotMixin(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Inject(method = "mayPlace", at = @At(value = "HEAD"), cancellable = true)
    public void mayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ItemInit.BACKPACK.get())){
            cir.setReturnValue(BackpackConfig.INSTANCE.canEquipFromInventory.get());
        }
    }
    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    public void mayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.getItem().is(ItemInit.BACKPACK.get())){
            cir.setReturnValue(BackpackConfig.INSTANCE.canEquipFromInventory.get());
        }
    }
}
