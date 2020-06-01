package me.crupette.cauldronoverhaul.util;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BucketActionTransformer {
    private static List<Entry> transformers = new ArrayList<>();

    public static void addTransformer(Entry entry){
        transformers.add(entry);
    }

    public static ItemStack onBucketFill(ItemStack root, Fluid fluid){
        for(Entry entry : transformers){
            root = entry.onBucketFill(root, fluid);
        }
        return root;
    }

    public static Fluid onBucketEmpty(ItemStack root, Fluid fluid){
        for(Entry entry : transformers){
            fluid = entry.onBucketEmpty(root, fluid);
        }
        return fluid;
    }

    public interface Entry {
        default ItemStack onBucketFill(ItemStack root, Fluid fluid) { return root; }
        default Fluid onBucketEmpty(ItemStack root, Fluid fluid) { return fluid; }
    }
}
