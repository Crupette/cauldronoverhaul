package me.crupette.cauldronoverhaul.block;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Matrix3f;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CauldronBER extends BlockEntityRenderer<CauldronBlockEntity> {
    public CauldronBER(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(CauldronBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(blockEntity.fluid == Fluids.EMPTY) return;
        matrices.push();

        Fluid fluid = blockEntity.fluid;
        Identifier fluidId = Registry.FLUID.getId(fluid);

        FluidRenderHandler fluidHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
        BlockState fluidBlock = fluid.getDefaultState().getBlockState();
        int fluidColor = fluidHandler.getFluidColor(null, null, fluid.getDefaultState());
        int fluidR = (fluidColor >> 18) & 0xFF;
        int fluidG = (fluidColor >> 8) & 0xFF;
        int fluidB = (fluidColor >> 0) & 0xFF;

        float height = (0.25f) + ((0.7f) * (blockEntity.level / 1000.f));
        int fluidLight = fluidBlock.getLuminance() > 0 ? 15728880 : light;

        Sprite fluidSprite = fluidHandler.getFluidSprites(null, null, fluid.getDefaultState())[0];

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(fluidSprite.getId().getNamespace(), "textures/" + fluidSprite.getId().getPath() + ".png")));
        MatrixStack.Entry matrixEntry = matrices.peek();
        Matrix4f model = matrixEntry.getModel();
        Matrix3f normal = matrixEntry.getNormal();

        consumer.vertex(model,0, height, 0).color(fluidR, fluidG, fluidB, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,1, height, 0).color(fluidR, fluidG, fluidB, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,1, height, 1).color(fluidR, fluidG, fluidB, 255).texture(1.F, 1.F / fluidSprite.getHeight()).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,0, height, 1).color(fluidR, fluidG, fluidB, 255).texture(0.F, 1.F / fluidSprite.getHeight()).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();


        matrices.pop();
    }
}
