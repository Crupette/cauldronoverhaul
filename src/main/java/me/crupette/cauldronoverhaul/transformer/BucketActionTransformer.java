package me.crupette.cauldronoverhaul.transformer;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

//Transformer for CauldronActionBucket
public class BucketActionTransformer {
    private static final List<Entry> transformers = new ArrayList<>();

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

        /*  Takes bucket itemstack and processes based on fluid information
        *   return: new processed item stack
        * */
        default ItemStack onBucketFill(ItemStack root, Fluid fluid) { return root; }

        /*  Takes fluid and processes based on itemstack information (for weird fluids)
        *   return: new processed fluid
        * */
        default Fluid onBucketEmpty(ItemStack root, Fluid fluid) { return fluid; }
    }
}
