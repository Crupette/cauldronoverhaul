package me.crupette.cauldronoverhaul;

import me.crupette.cauldronoverhaul.actions.*;
import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.integration.fluidpotions.FluidPotionsModIntegration;
import me.crupette.cauldronoverhaul.integration.potiontipped.PotionTippedModIntegration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CauldronOverhaul implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "cauldronoverhaul";
    public static final String MOD_NAME = "Cauldron Overhaul";

    public static BlockEntityType<CauldronBlockEntity> CAULDRON_BLOCK_ENTITY;

    @Override
    public void onInitialize() {
        CAULDRON_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "cauldron"),
                BlockEntityType.Builder.create(CauldronBlockEntity::new, Blocks.CAULDRON).build(null));

        CauldronActions.addAction(new CauldronActionBucket());
        CauldronActions.addAction(new CauldronActionBottle());
        CauldronActions.addAction(new CauldronActionClean());
        CauldronActions.addAction(new CauldronActionDye());

        if(FabricLoader.getInstance().isModLoaded("fluidpotions")){
            new FluidPotionsModIntegration().init();
            if(FabricLoader.getInstance().isModLoaded("potiontipped")){
                new PotionTippedModIntegration().init();
            }
        }
    }

    public static void log(Level level, String message){
        LOGGER.log(level, message);
    }

}