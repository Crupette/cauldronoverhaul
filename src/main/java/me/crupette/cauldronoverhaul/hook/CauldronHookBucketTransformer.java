package me.crupette.cauldronoverhaul.hook;

import me.crupette.cauldronoverhaul.api.FluidVolume;
import me.crupette.cauldronoverhaul.api.HookTransformer;
import me.crupette.cauldronoverhaul.mixin.BucketItemAccessor;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;

public class CauldronHookBucketTransformer implements HookTransformer.Entry {
    @Override
    public ItemStack onBucketFill(ItemStack stack, Fluid fluid) {
        return new ItemStack(fluid.getBucketItem());
    }

    @Override
    public Fluid onBucketEmpty(ItemStack stack, Fluid fluid) {
        if(stack.getItem() instanceof BucketItem){
            return ((BucketItemAccessor)stack.getItem()).getFluid();
        }
        return fluid;
    }
}
