package com.adrilasar.nomod.event;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.entity.custom.GuardianRuinas;
import com.adrilasar.nomod.entity.custom.ModEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NomoD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.GUARDIAN_R.get(), GuardianRuinas.setAttributes());
    }
}