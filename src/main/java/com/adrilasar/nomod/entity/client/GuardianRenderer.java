package com.adrilasar.nomod.entity.client;

import com.adrilasar.nomod.NomoD;
import com.adrilasar.nomod.entity.custom.GuardianRuinas;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GuardianRenderer extends GeoEntityRenderer<GuardianRuinas> {

    public GuardianRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GuardianModel());
        this. shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(GuardianRuinas instance) {
        return new ResourceLocation(NomoD.MOD_ID, "textures/entity/guardian/guardian.png");
    }

    @Override
    public RenderType getRenderType(GuardianRuinas animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(2F, 2F, 2F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
