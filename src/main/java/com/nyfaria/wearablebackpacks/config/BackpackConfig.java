package com.nyfaria.wearablebackpacks.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BackpackConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final BackpackConfig INSTANCE;


    static {
        Pair<BackpackConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(BackpackConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }


    public ForgeConfigSpec.BooleanValue inventoryToolTip;
    public ForgeConfigSpec.BooleanValue bundleAdd;
    public ForgeConfigSpec.BooleanValue autoAddToBag;
    public ForgeConfigSpec.BooleanValue canEquipFromInventory;
    public ForgeConfigSpec.IntValue rows;
    public ForgeConfigSpec.IntValue columns;
    public ForgeConfigSpec.IntValue entityBackpackChance;
    public ForgeConfigSpec.BooleanValue canOpenWhileEquipped;
    public ForgeConfigSpec.BooleanValue canEnchantBackpack;
    public ForgeConfigSpec.IntValue backpackDefenseLevel;
    public ForgeConfigSpec.IntValue backpackDurability;
    public ForgeConfigSpec.BooleanValue canOpenWithHand;
    public ForgeConfigSpec.BooleanValue canOpenOthers;
    public ForgeConfigSpec.BooleanValue useChestSlot;




    private BackpackConfig(ForgeConfigSpec.Builder builder) {
        this.canEquipFromInventory = builder.comment("Should you be able to equip from inventory?").define("canEquipFromInventory", false);
        this.autoAddToBag = builder.comment("Should items you pick up be automatically added to your backpack?").define("autoAddToBag", false);
        this.bundleAdd = builder.comment("Can you add stuff to your backpack like you would a Bundle?").define("bundleAdd", false);
        this.inventoryToolTip = builder.comment("Should the tooltip display the inventory?").define("inventoryToolTip", false);
        this.backpackDefenseLevel = builder.comment("How much armor does the backpack provide?").defineInRange("backpackDefenseLevel", 2, 1, 20);
        this.entityBackpackChance = builder.comment("How likely is it that a mob will drop a backpack?").defineInRange("entityBackpackChance", 10, 0, 100);
        this.backpackDurability = builder.comment("How much durability does the backpack have?").defineInRange("backpackDurability", 80, 1, 9999);
        this.canOpenWhileEquipped = builder.comment("Can you use a keybind to open an equipped Backpack?").define("canOpenWhileEquipped", false);
        this.canEnchantBackpack = builder.comment("Can you enchant your backpack?").define("canEnchantBackpack", true);
        this.canOpenWithHand = builder.comment("Can you open your backpack with your hand?").define("canOpenWithHand", false);
        this.canOpenOthers = builder.comment("Can you open other people's backpacks?").define("canOpenOthers", false);
        this.rows = builder.comment("How many rows does your backpack have?").defineInRange("rows", 4, 1, 13);
        this.columns = builder.comment("How many columns does your backpack have?").defineInRange("columns", 9, 1, 20);
        this.useChestSlot = builder.comment("Should the backpack be equipped in the armor slot?").define("useChestSlot", true);
    }

}