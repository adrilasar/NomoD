package com.adrilasar.nomod;

import com.adrilasar.nomod.block.ModBlocks;
import com.adrilasar.nomod.container.ModContainers;
import com.adrilasar.nomod.entity.client.GuardianRenderer;
import com.adrilasar.nomod.entity.custom.ModEntityTypes;
import com.adrilasar.nomod.item.ModItems;
import com.adrilasar.nomod.screen.KalcenitaFurnaceScreen;
import com.adrilasar.nomod.sound.ModSounds;
import com.adrilasar.nomod.tileentity.ModTileEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NomoD.MOD_ID)
public class NomoD
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "nomod";

    public NomoD() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModSounds.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GUARDIAN_R.get(), GuardianRenderer::new);
        RenderTypeLookup.setRenderLayer(ModBlocks.SPEAKER.get(), RenderType.solid());
        ScreenManager.register(ModContainers.KALCENITA_FURNACE_CONTAINER.get(),
                KalcenitaFurnaceScreen::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("LET'S PREINIT THAT SHIT...");
    }
}
