package me.crupette.cauldronoverhaul.integration.fluidpotions.transformers;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.transformer.CauldronBlockTransformer;
import me.crupette.fluidpotions.fluid.PotionFluid;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.Fluids;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionCauldronBlockTransformer implements CauldronBlockTransformer.Entry {
    public boolean onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CauldronBlockEntity blockEntity){
        if(!(blockEntity.fluid instanceof PotionFluid)) return false;
        if(!(entity instanceof LivingEntity)) return false;
        PotionFluid potionFluid = (PotionFluid) blockEntity.fluid;
        Potion potion = potionFluid.getPotion();

        //Fixes problem with potion fluids deleting the cauldron when deleted.
        if(potion.hasInstantEffect()){
            for(StatusEffectInstance effectInstance : potion.getEffects()){
                effectInstance.getEffectType().applyInstantEffect(entity, entity, (LivingEntity)entity,
                        effectInstance.getAmplifier() * 3, 1.0D);
            }
            blockEntity.fluid = Fluids.EMPTY;
            blockEntity.setLevel(0);
            blockEntity.sync();
            return true;
        }
        return false;
    }
}
