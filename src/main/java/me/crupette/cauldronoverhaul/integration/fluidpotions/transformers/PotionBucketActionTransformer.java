package me.crupette.cauldronoverhaul.integration.fluidpotions.transformers;

import me.crupette.cauldronoverhaul.transformer.BucketActionTransformer;
import me.crupette.fluidpotions.fluid.PotionFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;

//Ensures that potions being picked up by buckets won't be mangled
public class PotionBucketActionTransformer implements BucketActionTransformer.Entry {

    public ItemStack onBucketFill(ItemStack root, Fluid fluid) {
        if(fluid instanceof PotionFluid){
            PotionUtil.setPotion(root, ((PotionFluid) fluid).getPotion());
        }
        return root;
    }
}
