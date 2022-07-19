package com.adrilasar.nomod.data.recipes;

import com.adrilasar.nomod.NomoD;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface IKalcenitaRefineryRecipe extends IRecipe<IInventory>
{
    ResourceLocation TYPE_ID = new ResourceLocation(NomoD.MOD_ID, "refinery");

    @Override
    default IRecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    default boolean isSpecial(){
        return true;
    }
}
