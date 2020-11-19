package me.crupette.cauldronoverhaul.block;

import me.crupette.cauldronoverhaul.CauldronOverhaul;
import me.crupette.cauldronoverhaul.block.entity.COBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class COBlocks {

    public static Block CAULDRON_OVERHAULED = new OverhauledCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON));

    public static void init(){
        Registry.register(Registry.BLOCK, Registry.BLOCK.getRawId(Blocks.CAULDRON), "cauldron", CAULDRON_OVERHAULED);
        Registry.register(Registry.ITEM, Registry.ITEM.getRawId(Items.CAULDRON), "cauldron", new BlockItem(CAULDRON_OVERHAULED, new Item.Settings().group(ItemGroup.DECORATIONS)));

        COBlockEntities.init();
    }
}
