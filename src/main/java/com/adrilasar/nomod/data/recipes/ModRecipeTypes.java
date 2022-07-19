package com.adrilasar.nomod.data.recipes;

import com.adrilasar.nomod.NomoD;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NomoD.MOD_ID);

    public static final RegistryObject<KalcenitaRefineryRecipe.Serializer> REFINERY_SERIALIZER
            = RECIPE_SERIALIZER.register("refinery", KalcenitaRefineryRecipe.Serializer::new);

    public static IRecipeType<KalcenitaRefineryRecipe> REFINERY_RECIPE
            = new KalcenitaRefineryRecipe.KalcenitaRefineryRecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, KalcenitaRefineryRecipe.TYPE_ID, REFINERY_RECIPE);
    }
}
