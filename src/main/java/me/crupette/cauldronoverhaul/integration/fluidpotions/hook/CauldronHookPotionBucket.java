package me.crupette.cauldronoverhaul.integration.fluidpotions.hook;

import me.crupette.cauldronoverhaul.api.CauldronHook;
import me.crupette.cauldronoverhaul.api.FluidVolume;
import me.crupette.cauldronoverhaul.api.HookTransformer;
import me.crupette.cauldronoverhaul.block.entity.CauldronBlockEntity;
import me.crupette.fluidpotions.FluidPotions;
import me.crupette.fluidpotions.item.PotionBucketItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

public class CauldronHookPotionBucket implements CauldronHook {

    public CauldronHookPotionBucket(){
        HookTransformer.addTransformer(new CauldronHookBucketPotionTransformer());
        System.out.println("Added hook for potionfluids");
    }

    @Override
    public ActionResult onUse(CauldronBlockEntity blockEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if(item instanceof PotionBucketItem){
            Fluid fluid = FluidPotions.getStill(PotionUtil.getPotion(itemStack));

            FluidVolume insert = new FluidVolume(fluid, Fraction.ONE);
            if(blockEntity.getSlot(0).insert(insert, false) == insert) return ActionResult.PASS;

            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.f, 1.f);
            if(!player.isCreative()){
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
            }
            blockEntity.markDirty();
            if(!world.isClient) blockEntity.sync();
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
}
