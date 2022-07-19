package com.adrilasar.nomod.data.recipes;

import com.adrilasar.nomod.block.ModBlocks;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class KalcenitaRefineryRecipe implements IKalcenitaRefineryRecipe
{
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public KalcenitaRefineryRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(IInventory pInv, World pLevel) {
        return testItem(0, pInv) && testItem(1, pInv) && testItem(2, pInv);
    }

    private boolean testItem(int slot,IInventory pInv) {
        return recipeItems.get(slot).test(pInv.getItem(slot));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(IInventory pInv) {
        return output.copy();
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.KALCENITA_REFINERY.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.REFINERY_SERIALIZER.get();
    }

    public static class KalcenitaRefineryRecipeType implements IRecipeType<KalcenitaRefineryRecipe> {
        @Override
        public String toString() {
            return KalcenitaRefineryRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<KalcenitaRefineryRecipe> {

        @Override
        public KalcenitaRefineryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new KalcenitaRefineryRecipe(recipeId, output, inputs);
        }

        @Nullable
        @Override
        public KalcenitaRefineryRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack output = buffer.readItem();
            return new KalcenitaRefineryRecipe(recipeId, output, inputs);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, KalcenitaRefineryRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.output, false);
        }
    }
}
