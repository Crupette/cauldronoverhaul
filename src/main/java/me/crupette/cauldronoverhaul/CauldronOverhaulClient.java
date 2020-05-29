package me.crupette.cauldronoverhaul;

import me.crupette.cauldronoverhaul.block.CauldronBER;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class CauldronOverhaulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(CauldronOverhaul.CAULDRON_BLOCK_ENTITY, CauldronBER::new);
    }
}
