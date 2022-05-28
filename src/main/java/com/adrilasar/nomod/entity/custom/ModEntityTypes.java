package com.adrilasar.nomod.entity.custom;

import com.adrilasar.nomod.NomoD;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, NomoD.MOD_ID);

    public static final RegistryObject<EntityType<GuardianRuinas>> GUARDIAN_R =
            ENTITY_TYPES.register("guardian",
                    () -> EntityType.Builder.of(GuardianRuinas::new, EntityClassification.MONSTER)
                            .sized(2.8f, 5.6f)
                            .build(new ResourceLocation(NomoD.MOD_ID, "guardian").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
