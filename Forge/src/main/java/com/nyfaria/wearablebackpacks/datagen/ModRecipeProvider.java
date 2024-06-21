package com.nyfaria.wearablebackpacks.datagen;

import com.nyfaria.wearablebackpacks.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput generator) {
        super(generator);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeSaver) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,ItemInit.BACKPACK.get())
                .pattern("LGL")
                .pattern("LWL")
                .pattern("LLL")
                .define('L', Items.LEATHER)
                .define('W', ItemTags.WOOL)
                .define('G', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_leather", has(Tags.Items.LEATHER))
                .save(recipeSaver);
    }
}
