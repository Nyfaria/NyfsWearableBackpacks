package com.nyfaria.wearablebackpacks.datagen;

import com.nyfaria.wearablebackpacks.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> recipeSaver) {
        ShapedRecipeBuilder.shaped(ItemInit.BACKPACK.get())
                .pattern("LGL")
                .pattern("LWL")
                .pattern("LLL")
                .define('L', Tags.Items.LEATHER)
                .define('W', ItemTags.WOOL)
                .define('G', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_leather", has(Tags.Items.LEATHER))
                .save(recipeSaver);
    }
}