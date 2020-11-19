package me.crupette.cauldronoverhaul.integration;

import me.crupette.cauldronoverhaul.integration.fluidpotions.COFluidPotions;
import net.fabricmc.loader.api.FabricLoader;

public class COModIntegration {

    public static void register(){
        if(FabricLoader.getInstance().isModLoaded("fluidpotions")){
            COFluidPotions.init();
        }
    }
}
