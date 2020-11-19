package me.crupette.cauldronoverhaul.integration.fluidpotions;

import me.crupette.cauldronoverhaul.block.entity.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.integration.fluidpotions.hook.CauldronHookPotion;
import me.crupette.cauldronoverhaul.integration.fluidpotions.hook.CauldronHookPotionBucket;

public class COFluidPotions {

    public static void init(){
        CauldronBlockEntity.hooks.add(new CauldronHookPotionBucket());
        CauldronBlockEntity.hooks.add(new CauldronHookPotion());
    }
}
