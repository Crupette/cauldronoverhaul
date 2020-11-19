package me.crupette.cauldronoverhaul.hook;

import me.crupette.cauldronoverhaul.api.CauldronHook;
import me.crupette.cauldronoverhaul.api.FluidVolume;
import me.crupette.cauldronoverhaul.block.entity.CauldronBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

public class CauldronHookClean implements CauldronHook {

    @Override
    public ActionResult onUse(CauldronBlockEntity blockEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if(blockEntity.getSlot(0).getVolume().getFluid() != Fluids.WATER) return ActionResult.PASS;

        FluidVolume bottle = new FluidVolume(Fluids.WATER, Fraction.ONE_THIRD);
        if(blockEntity.getSlot(0).extract(bottle, true) != bottle) return ActionResult.PASS;

        if(item instanceof DyeableItem){
            DyeableItem dyeableItem = (DyeableItem)item;
            if(dyeableItem.hasColor(itemStack) && !world.isClient){
                dyeableItem.removeColor(itemStack);
                player.incrementStat(Stats.CLEAN_ARMOR);
                blockEntity.getSlot(0).extract(bottle, false);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.f, 1.f);
                blockEntity.markDirty();
                blockEntity.sync();
            }
            return ActionResult.success(world.isClient);
        }else if(item instanceof BannerItem){
            if(BannerBlockEntity.getPatternCount(itemStack) > 0 && !world.isClient){
                ItemStack bannerCopy = itemStack.copy();
                bannerCopy.setCount(1);
                BannerBlockEntity.loadFromItemStack(bannerCopy);
                player.incrementStat(Stats.CLEAN_BANNER);
                if(!player.abilities.creativeMode){
                    itemStack.decrement(1);
                    blockEntity.getSlot(0).extract(bottle, false);
                }
                if(itemStack.isEmpty()){
                    player.setStackInHand(hand, bannerCopy);
                }else if(!player.inventory.insertStack(bannerCopy)){
                    player.dropItem(bannerCopy, false);
                }else if(player instanceof ServerPlayerEntity){
                    ((ServerPlayerEntity)player).openHandledScreen(player.playerScreenHandler);
                }
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.f, 1.f);
                blockEntity.markDirty();
                blockEntity.sync();
            }
            return ActionResult.success(world.isClient);
        }else if(item instanceof BlockItem){
            Block block = ((BlockItem)item).getBlock();
            if(block instanceof ShulkerBoxBlock){
                if(!world.isClient){
                    ItemStack shulkerCopy = itemStack.copy();
                    shulkerCopy.setCount(1);

                    player.setStackInHand(hand, shulkerCopy);
                    blockEntity.getSlot(0).extract(bottle, false);
                    player.incrementStat(Stats.CLEAN_SHULKER_BOX);
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.f, 1.f);
                }
            }
        }

        return ActionResult.PASS;
    }
}
