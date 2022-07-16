package com.adrilasar.nomod.block;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.block.custom.KalcenitaFurnaceBlock;
import com.adrilasar.nomod.block.custom.Speaker;
import com.adrilasar.nomod.item.ModItemGroup;
import com.adrilasar.nomod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks
{
    //BLOCKS
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, NomoD.MOD_ID);

    public static final RegistryObject<Block> KALCENITA_BLOCK = registerBlock("kalcenita_block",
            () -> new Block(AbstractBlock.Properties.of(Material.METAL).strength(50.0F, 1200.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE)),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> KALCENITA_ORE = registerBlock("kalcenita_ore",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(30.0F, 1200.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(4)),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> RAW_IRON_BLOCK = registerBlock("raw_iron_block",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(1.5F, 6.0F).requiresCorrectToolForDrops()),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> SPEAKER = registerBlock("speaker",
            () -> new Speaker(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F, 6.0F).noOcclusion()),
            ModItemGroup.NOMOD_TAB);

    public static final RegistryObject<Block> KALCENITA_FURNACE = registerBlock("kalcenita_furnace",
            () -> new KalcenitaFurnaceBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F)),
            ModItemGroup.NOMOD_TAB);

    //REGISTRIES
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
