package me.crupette.cauldronoverhaul.hook;

import me.crupette.cauldronoverhaul.api.CauldronHook;
import me.crupette.cauldronoverhaul.api.FluidVolume;
import me.crupette.cauldronoverhaul.api.HookTransformer;
import me.crupette.cauldronoverhaul.api.Tank;
import me.crupette.cauldronoverhaul.block.entity.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.mixin.BucketItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

public class CauldronHookBucket implements CauldronHook {

    public CauldronHookBucket(){
        HookTransformer.addTransformer(new CauldronHookBucketTransformer());
    }

    @Override
    public int getComparatorOutput(CauldronBlockEntity blockEntity, BlockState state, World world, BlockPos pos) {
        Fraction fraction = blockEntity.getSlot(0).getVolume().getCount();
        return (int)(fraction.floatValue() * 4);
    }

    private void playFillSound(Fluid fluid, World world, BlockPos pos){
        if(fluid.matchesType(Fluids.LAVA)){
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.f, 1.f);
        }else{
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.f, 1.f);
        }
    }

    private void playEmptySound(Fluid fluid, World world, BlockPos pos) {
        if(fluid.matchesType(Fluids.LAVA)){
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.f, 1.f);
        }else{
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.f, 1.f);
        }
    }

    @Override
    public ActionResult onUse(CauldronBlockEntity blockEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.isEmpty()) return ActionResult.PASS;

        Item item = itemStack.getItem();
        if(item instanceof BucketItem){
            Fluid bucketFluid = HookTransformer.onBucketEmpty(itemStack, Fluids.EMPTY);
            FluidVolume cauldronFluidVolume = blockEntity.getSlot(0).getVolume();
            if(bucketFluid.equals(Fluids.EMPTY)){
                FluidVolume extract = new FluidVolume(cauldronFluidVolume.getFluid(), Fraction.ONE);
                Fluid cauldronFluid = cauldronFluidVolume.getFluid();
                if(blockEntity.getSlot(0).extract(extract, true) == extract){
                    playEmptySound(extract.getFluid(), world, pos);
                    blockEntity.getSlot(0).extract(extract, false);
                    if(!player.isCreative()){
                        player.setStackInHand(hand, HookTransformer.onBucketFill(new ItemStack(cauldronFluid.getBucketItem()), cauldronFluid));
                    }
                    blockEntity.markDirty();
                    if(!world.isClient) blockEntity.sync();
                    return ActionResult.success(world.isClient);
                }

            }else
            if(cauldronFluidVolume.isEmpty() ||
                    (cauldronFluidVolume.getFluid().equals(bucketFluid) && cauldronFluidVolume.getCount().floatValue() < 1.f)){
                blockEntity.getSlot(0).insert(new FluidVolume(bucketFluid, Fraction.ONE), false);
                playFillSound(bucketFluid, world, pos);
                if(!player.isCreative()){
                    player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                }
                blockEntity.markDirty();
                if(!world.isClient) blockEntity.sync();
                return ActionResult.success(world.isClient);
            }
            return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }
}
