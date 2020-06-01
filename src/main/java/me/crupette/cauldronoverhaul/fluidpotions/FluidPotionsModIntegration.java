package me.crupette.cauldronoverhaul.fluidpotions;

import me.crupette.cauldronoverhaul.actions.CauldronActions;
import me.crupette.cauldronoverhaul.util.BucketActionTransformer;

public class FluidPotionsModIntegration {

    public void init(){
        CauldronActions.addAction(new CauldronActionPotion());

        BucketActionTransformer.addTransformer(new PotionBucketActionTransformer());
    }
}
