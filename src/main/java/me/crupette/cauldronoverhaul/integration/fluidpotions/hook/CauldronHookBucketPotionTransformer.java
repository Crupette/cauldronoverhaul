package me.crupette.cauldronoverhaul.integration.fluidpotions.hook;

import me.crupette.cauldronoverhaul.api.HookTransformer;
import me.crupette.cauldronoverhaul.mixin.BucketItemAccessor;
import me.crupette.fluidpotions.FluidPotions;
import me.crupette.fluidpotions.fluid.PotionFluid;
import me.crupette.fluidpotions.item.PotionBucketItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;

public class CauldronHookBucketPotionTransformer implements HookTransformer.Entry {

    @Override
    public ItemStack onBucketFill(ItemStack stack, Fluid fluid) {
        if(stack.getItem() instanceof PotionBucketItem){
            PotionUtil.setPotion(stack, ((PotionFluid)fluid).getPotion());
        }
        return stack;
    }
}
