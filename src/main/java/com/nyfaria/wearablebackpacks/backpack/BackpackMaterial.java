package com.nyfaria.wearablebackpacks.backpack;

import java.util.function.Supplier;

import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.LazyValue;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

public enum BackpackMaterial implements IArmorMaterial {
   LEATHER("leather", 5, new int[]{1, 2, BackpackConfig.INSTANCE.backpackDefenseLevel.get(), 1}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
      return Ingredient.of(Items.LEATHER);
   });

   private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
   private final String name;
   private final int durabilityMultiplier;
   private final int[] slotProtections;
   private final int enchantmentValue;
   private final SoundEvent sound;
   private final float toughness;
   private final float knockbackResistance;
   private final LazyValue<Ingredient> repairIngredient;

   BackpackMaterial(String p_40474_, int p_40475_, int[] p_40476_, int p_40477_, SoundEvent p_40478_, float p_40479_, float p_40480_, Supplier<Ingredient> p_40481_) {
      this.name = p_40474_;
      this.durabilityMultiplier = p_40475_;
      this.slotProtections = p_40476_;
      this.enchantmentValue = p_40477_;
      this.sound = p_40478_;
      this.toughness = p_40479_;
      this.knockbackResistance = p_40480_;
      this.repairIngredient = new LazyValue<>(p_40481_);
   }

   public int getDurabilityForSlot(EquipmentSlotType pSlot) {
      return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
   }

   public int getDefenseForSlot(EquipmentSlotType pSlot) {
      return this.slotProtections[pSlot.getIndex()];
   }

   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   public SoundEvent getEquipSound() {
      return this.sound;
   }

   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }

   public String getName() {
      return this.name;
   }

   public float getToughness() {
      return this.toughness;
   }

   /**
    * Gets the percentage of knockback resistance provided by armor of the material.
    */
   public float getKnockbackResistance() {
      return this.knockbackResistance;
   }
}