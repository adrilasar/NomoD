package com.adrilasar.nomod.item;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.entity.custom.ModEntityTypes;
import com.adrilasar.nomod.item.custom.KalcenitaSword;
import com.adrilasar.nomod.item.custom.SpeakerUsb;
import com.adrilasar.nomod.sound.ModSounds;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NomoD.MOD_ID);

    //ITEMS
    public static final RegistryObject<Item> NOMOD_CHARM = ITEMS.register("nomod_charm",
            () -> new Item(new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    public static final RegistryObject<Item> GUARDIAN_SPAWN_EGG = ITEMS.register("guardian_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GUARDIAN_R,0x5D5955, 0xE06200,
                    new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> KALCENITA_INGOT = ITEMS.register("kalcenita_ingot",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> RAW_KALCENITA = ITEMS.register("raw_kalcenita",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> MONSERRAT_MUSIC_USB = ITEMS.register("monserrat_music_usb",
            () -> new SpeakerUsb(4, ModSounds.MONSERRAT,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB).stacksTo(1)));

    public static final RegistryObject<Item> DANDY_G_MUSIC_USB = ITEMS.register("dandy_g_music_usb",
            () -> new SpeakerUsb(4, ModSounds.EJEMPLO_2,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB).stacksTo(1)));

    public static final RegistryObject<Item> FIUFIU_MUSIC_USB = ITEMS.register("fiufiu_music_usb",
            () -> new SpeakerUsb(4, ModSounds.FIUFIU,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB).stacksTo(1)));

    public static final RegistryObject<Item> KALCENITA_SWORD = ITEMS.register("kalcenita_sword",
            () -> new KalcenitaSword(ModItemTier.KALCENITA, 3, -2.4F,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    public static final RegistryObject<Item> KALCENITA_PICKAXE = ITEMS.register("kalcenita_pickaxe",
            () -> new PickaxeItem(ModItemTier.KALCENITA, 1, -2.8F,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    public static final RegistryObject<Item> KALCENITA_SHOVEL = ITEMS.register("kalcenita_shovel",
            () -> new ShovelItem(ModItemTier.KALCENITA, 1.5F, -3.0F,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    public static final RegistryObject<Item> KALCENITA_AXE = ITEMS.register("kalcenita_axe",
            () -> new AxeItem(ModItemTier.KALCENITA, 4.5F, -2.9F,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    public static final RegistryObject<Item> KALCENITA_HOE = ITEMS.register("kalcenita_hoe",
            () -> new HoeItem(ModItemTier.KALCENITA, -5, 0.0F,
                    new Item.Properties().tab(ModItemGroup.NOMOD_TAB)));

    //

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
