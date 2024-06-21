package com.nyfaria.wearablebackpacks.mixin;

import com.nyfaria.wearablebackpacks.client.model.CustomModel;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.function.Consumer;

@Mixin(BackpackItem.class)
public abstract class BackpackItemMixin extends Item {
    public BackpackItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return BackpackConfig.INSTANCE.canEquipFromInventory.get();
    }
    @Override
    public int getMaxDamage(ItemStack stack) {
        return BackpackConfig.INSTANCE.backpackDurability.get();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                                                          EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return new CustomModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CustomModel.LAYER_LOCATION));
            }

        });
    }
}
