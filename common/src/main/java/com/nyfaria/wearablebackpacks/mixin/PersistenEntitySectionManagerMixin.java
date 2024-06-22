package com.nyfaria.wearablebackpacks.mixin;

import com.nyfaria.wearablebackpacks.CommonClass;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public class PersistenEntitySectionManagerMixin {
    @Inject(method = "addEntity", at = @At("HEAD"))
    private void addEntity(EntityAccess pEntity, boolean pWorldGenSpawned, CallbackInfoReturnable<Boolean> cir) {
            CommonClass.equipBackPack(pEntity);
    }
}
