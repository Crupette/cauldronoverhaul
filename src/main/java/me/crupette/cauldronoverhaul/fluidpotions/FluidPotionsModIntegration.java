package me.crupette.cauldronoverhaul.fluidpotions;

import me.crupette.cauldronoverhaul.actions.CauldronActions;
import me.crupette.cauldronoverhaul.fluidpotions.actions.CauldronActionFuel;
import me.crupette.cauldronoverhaul.fluidpotions.actions.CauldronActionIngredient;
import me.crupette.cauldronoverhaul.fluidpotions.actions.CauldronActionPotion;
import me.crupette.cauldronoverhaul.fluidpotions.transformers.PotionBucketActionTransformer;
import me.crupette.cauldronoverhaul.fluidpotions.transformers.PotionCauldronBlockEntityTransformer;
import me.crupette.cauldronoverhaul.fluidpotions.transformers.PotionCauldronBlockTransformer;
import me.crupette.cauldronoverhaul.transformer.BucketActionTransformer;
import me.crupette.cauldronoverhaul.transformer.CauldronBlockEntityTransformer;
import me.crupette.cauldronoverhaul.transformer.CauldronBlockTransformer;

public class FluidPotionsModIntegration {

    public void init(){
        CauldronActions.addAction(new CauldronActionPotion());
        CauldronActions.addAction(new CauldronActionFuel());
        CauldronActions.addAction(new CauldronActionIngredient());

        BucketActionTransformer.addTransformer(new PotionBucketActionTransformer());
        CauldronBlockTransformer.addTransformer(new PotionCauldronBlockTransformer());
        CauldronBlockEntityTransformer.addTransformer(new PotionCauldronBlockEntityTransformer());
    }
}
