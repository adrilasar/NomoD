package com.adrilasar.nomod.item;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.entity.custom.ModEntityTypes;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NomoD.MOD_ID);

    public static final RegistryObject<Item> GUARDIAN_SPAWN_EGG = ITEMS.register("guardian_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GUARDIAN_R,0x5D5955, 0xE06200,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    //TODO Cambiar nombre
    public static final RegistryObject<Item> NUEVO_MINERAL = ITEMS.register("nuevo_mineral",
            () -> new Item(new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    //TODO Cambiar nombre
    public static final RegistryObject<Item> RAW_NUEVO_MINERAL = ITEMS.register("raw_nuevo_mineral",
            () -> new Item(new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
