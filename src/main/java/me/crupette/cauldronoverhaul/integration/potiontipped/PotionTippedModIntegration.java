package me.crupette.cauldronoverhaul.integration.potiontipped;

import me.crupette.cauldronoverhaul.actions.CauldronActions;
import me.crupette.cauldronoverhaul.integration.potiontipped.actions.CauldronActionDip;

public class PotionTippedModIntegration {

    public void init(){
        CauldronActions.addAction(new CauldronActionDip());
    }
}
