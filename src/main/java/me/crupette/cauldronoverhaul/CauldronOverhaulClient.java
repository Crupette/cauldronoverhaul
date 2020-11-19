package me.crupette.cauldronoverhaul;

import me.crupette.cauldronoverhaul.block.entity.COBlockEntities;
import me.crupette.cauldronoverhaul.block.renderer.OverhauledCauldronBER;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class CauldronOverhaulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(COBlockEntities.CAULDRON_BLOCK_ENTITY, OverhauledCauldronBER::new);
    }
}
