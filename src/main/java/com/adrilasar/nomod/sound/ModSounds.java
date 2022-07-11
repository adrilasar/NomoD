package com.adrilasar.nomod.sound;

import com.adrilasar.nomod.NomoD;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds
{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, NomoD.MOD_ID);

    //SONIDOS
    public static final RegistryObject<SoundEvent> EJEMPLO_1 = registerSoundEvent("ejemplo_1");

    public static final RegistryObject<SoundEvent> EJEMPLO_2 = registerSoundEvent("ejemplo_2");

    //

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(NomoD.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
