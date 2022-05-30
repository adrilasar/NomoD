package com.adrilasar.nomod.block;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.item.ModItemGroup;
import com.adrilasar.nomod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, NomoD.MOD_ID);

    //TODO Cambiar nombre
    public static final RegistryObject<Block> NUEVO_MINERAL_BLOCK = registerBlock("nuevo_mineral_block",
            () -> new Block(AbstractBlock.Properties.of(Material.METAL).strength(9f)),
            ModItemGroup.NOMOD_TAB);

    //TODO Cambiar nombre
    public static final RegistryObject<Block> NUEVO_MINERAL_ORE = registerBlock("raw_nuevo_mineral_block",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(9f).requiresCorrectToolForDrops()),
            ModItemGroup.NOMOD_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, ItemGroup tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, ItemGroup tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
