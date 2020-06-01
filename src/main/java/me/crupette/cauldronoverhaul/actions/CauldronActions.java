package me.crupette.cauldronoverhaul.actions;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class CauldronActions {
    private static final List<ICauldronAction> cauldronActionList = new ArrayList<ICauldronAction>();

    public static void addAction(ICauldronAction action){
        cauldronActionList.add(action);
    }

    public static ImmutableList<ICauldronAction> getCauldronActions(){
        return ImmutableList.copyOf(cauldronActionList);
    }
}
