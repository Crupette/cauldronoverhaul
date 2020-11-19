package me.crupette.cauldronoverhaul.block.entity;

import me.crupette.cauldronoverhaul.CauldronOverhaul;
import me.crupette.cauldronoverhaul.block.COBlocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class COBlockEntities {

    public static BlockEntityType<CauldronBlockEntity> CAULDRON_BLOCK_ENTITY;

    public static void init(){
        CAULDRON_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CauldronOverhaul.id("cauldron"), BlockEntityType.Builder.create(CauldronBlockEntity::new, COBlocks.CAULDRON_OVERHAULED).build(null));
    }
}
