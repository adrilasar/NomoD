package com.adrilasar.nomod.entity.client;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.entity.custom.GuardianRuinas;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GuardianModel extends AnimatedGeoModel<GuardianRuinas> {

    @Override
    public ResourceLocation getModelLocation(GuardianRuinas object) {
        return new ResourceLocation(NomoD.MOD_ID, "geo/guardian.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GuardianRuinas object) {
        return new ResourceLocation(NomoD.MOD_ID, "textures/entity/guardian/guardian.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GuardianRuinas animatable) {
        return new ResourceLocation(NomoD.MOD_ID, "animations/guardian.animation.json");
    }
}
