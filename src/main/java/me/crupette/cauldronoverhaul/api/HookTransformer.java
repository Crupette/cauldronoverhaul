package me.crupette.cauldronoverhaul.api;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HookTransformer {
    private static final List<Entry> transformers = new ArrayList<>();

    public static void addTransformer(Entry entry){
        transformers.add(entry);
    }

    public static ItemStack onBucketFill(ItemStack stack, Fluid fluid){
        for(Entry entry : transformers){
            stack = entry.onBucketFill(stack, fluid);
        }
        return stack;
    }

    public static Fluid onBucketEmpty(ItemStack stack, Fluid fluid){
        for(Entry entry : transformers){
            fluid = entry.onBucketEmpty(stack, fluid);
        }
        return fluid;
    }

    public interface Entry {
        /*  Takes bucket itemstack and processes based on fluid information
         *   return: new processed item stack
         * */
        default ItemStack onBucketFill(ItemStack stack, Fluid fluid) { return stack; }

        /*  Takes fluid and processes based on itemstack information (for weird fluids)
         *   return: new processed fluid
         * */
        default Fluid onBucketEmpty(ItemStack stack, Fluid fluid) { return fluid; }
    }
}
