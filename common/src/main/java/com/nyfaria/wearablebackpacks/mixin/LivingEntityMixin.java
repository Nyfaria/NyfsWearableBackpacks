package com.nyfaria.wearablebackpacks.mixin;

import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot pSlot);

    @Inject(method = "broadcastBreakEvent(Lnet/minecraft/world/entity/EquipmentSlot;)V", at = @At("HEAD"))
    private void onBreakEvent(EquipmentSlot pSlot, CallbackInfo ci) {
        ItemStack item = this.getItemBySlot(pSlot);
        if (item.is(ItemInit.BACKPACK.get())) {
            Containers.dropContents(level(), this, BackpackItem.getInventory(item));
        }
    }
}
