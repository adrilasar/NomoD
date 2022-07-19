package com.adrilasar.nomod.tileentity;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, NomoD.MOD_ID);

    public static RegistryObject<TileEntityType<KalcenitaRefineryTile>> KALCENITA_REFINERY_TILE =
            TILE_ENTITIES.register("kalcenita_refinery_tile", () -> TileEntityType.Builder.of(
                    KalcenitaRefineryTile::new, ModBlocks.KALCENITA_REFINERY.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}