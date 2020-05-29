package me.crupette.cauldronoverhaul.actions;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
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
import net.minecraft.world.World;

public class CauldronActionBucket implements ICauldronAction{

    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.getItem() instanceof BucketItem){
            BucketItem bucket = (BucketItem) itemStack.getItem();
            //TODO: Get (or make) a fluid API that registers buckets of fluids with public fluid fields (god damn it Mojang)
            Fluid fluid = Fluids.EMPTY;
            if(bucket == Items.WATER_BUCKET) fluid = Fluids.WATER;
            if(bucket == Items.LAVA_BUCKET) fluid = Fluids.LAVA;

            if(bucket == Items.BUCKET){
                if(entity.level == 1000 && !world.isClient){
                    if(!player.abilities.creativeMode){
                        itemStack.decrement(1);
                        if(itemStack.isEmpty()){
                            //HMMMMMMMMMM
                            player.setStackInHand(hand, new ItemStack(entity.fluid.getBucketItem()));
                        }else if(!player.inventory.insertStack(new ItemStack(entity.fluid.getBucketItem()))){
                            player.dropItem(new ItemStack(entity.fluid.getBucketItem()), false);
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
