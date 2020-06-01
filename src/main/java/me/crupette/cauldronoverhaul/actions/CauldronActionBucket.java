package me.crupette.cauldronoverhaul.actions;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.util.BucketActionTransformer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.stream.Collectors;


public class CauldronActionBucket implements ICauldronAction{

    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.getItem() instanceof BucketItem){
            BucketItem bucket = (BucketItem) itemStack.getItem();

            Fluid fluid = Fluids.EMPTY;
            for(Fluid checkFluid : Registry.FLUID.stream().collect(Collectors.toSet())){
                if(checkFluid.isStill(checkFluid.getDefaultState()) && checkFluid.getBucketItem() == bucket){
                    fluid = checkFluid;
                    break;
                }
            }
            fluid = BucketActionTransformer.onBucketEmpty(itemStack, fluid);

            if(bucket == Items.BUCKET){
                if(entity.level == 1000 && !world.isClient){
                    if(!player.abilities.creativeMode){
                        itemStack.decrement(1);
                        ItemStack fluidBucket = new ItemStack(entity.fluid.getBucketItem());
                        fluidBucket = BucketActionTransformer.onBucketFill(fluidBucket, entity.fluid);
                        if(itemStack.isEmpty()){
                            player.setStackInHand(hand, fluidBucket);
                        }else if(!player.inventory.insertStack(fluidBucket)){
                            player.dropItem(fluidBucket, false);
                        }
                    }
                    player.incrementStat(Stats.USE_CAULDRON);
                    entity.fluid = Fluids.EMPTY;
                    entity.setLevel(0);
                    entity.markDirty();
                    world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResult.SUCCESS;
                }
            }else if(entity.level < 1000 && (entity.fluid == Fluids.EMPTY || fluid == entity.fluid) && !world.isClient){

                if(!player.abilities.creativeMode) {
                    player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                }
                player.incrementStat(Stats.FILL_CAULDRON);
                entity.fluid = fluid;
                entity.setLevel(1000);
                entity.markDirty();
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
