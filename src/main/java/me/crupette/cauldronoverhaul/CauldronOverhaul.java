package me.crupette.cauldronoverhaul;

import me.crupette.cauldronoverhaul.api.CauldronHook;
import me.crupette.cauldronoverhaul.block.COBlocks;
import me.crupette.cauldronoverhaul.block.entity.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.hook.CauldronHookBottle;
import me.crupette.cauldronoverhaul.hook.CauldronHookBucket;
import me.crupette.cauldronoverhaul.hook.CauldronHookClean;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CauldronOverhaul implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "cauldronoverhaul";
    public static final String MOD_NAME = "Cauldron Overhaul";

    @Override
    public void onInitialize() {
        COBlocks.init();

        CauldronBlockEntity.hooks.add(new CauldronHookBucket());
        CauldronBlockEntity.hooks.add(new CauldronHookBottle());
        CauldronBlockEntity.hooks.add(new CauldronHookClean());
    }

    public static void log(Level level, String message){
        LOGGER.log(level, message);
    }

    public static Identifier id(String name){
        return new Identifier(MOD_ID, name);
    }

}