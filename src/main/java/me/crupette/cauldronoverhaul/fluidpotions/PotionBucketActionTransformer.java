package me.crupette.cauldronoverhaul.fluidpotions;

import me.crupette.cauldronoverhaul.util.BucketActionTransformer;
import me.crupette.fluidpotions.fluid.PotionFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;

public class PotionBucketActionTransformer implements BucketActionTransformer.Entry {
    public ItemStack onBucketFill(ItemStack root, Fluid fluid) {
        if(fluid instanceof PotionFluid){
            root = PotionUtil.setPotion(root, ((PotionFluid)fluid).getPotion());
        }
        return root;
    }
}
