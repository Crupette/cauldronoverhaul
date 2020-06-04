package me.crupette.cauldronoverhaul.actions;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronActionClean implements ICauldronAction{
    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(entity.fluid != Fluids.WATER || !entity.takeBottle(true)) return ActionResult.PASS;
        if(itemStack.getItem() instanceof DyeableItem){
            DyeableItem dyeableItem = (DyeableItem)itemStack.getItem();
            if(dyeableItem.hasColor(itemStack) && !world.isClient){
                dyeableItem.removeColor(itemStack);
                player.incrementStat(Stats.CLEAN_ARMOR);
                entity.takeBottle(false);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.method_29236(world.isClient);
        }
        if(itemStack.getItem() instanceof BannerItem){
            if(BannerBlockEntity.getPatternCount(itemStack) > 0 && !world.isClient){
                ItemStack bannerCopy = itemStack.copy();
                bannerCopy.setCount(1);
                BannerBlockEntity.loadFromItemStack(bannerCopy);
                player.incrementStat(Stats.CLEAN_BANNER);
                if(!player.abilities.creativeMode){
                    itemStack.decrement(1);
                    entity.takeBottle(false);
                }
                if(itemStack.isEmpty()){
                    player.setStackInHand(hand, bannerCopy);
                }else if(!player.inventory.insertStack(bannerCopy)){
                    player.dropItem(bannerCopy, false);
                }else if(player instanceof ServerPlayerEntity){
                    ((ServerPlayerEntity)player).openHandledScreen(player.playerScreenHandler);
                }
            }
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.method_29236(world.isClient);
        }

        if(itemStack.getItem() instanceof BlockItem){
            Block block = ((BlockItem) itemStack.getItem()).getBlock();
            if(block instanceof ShulkerBoxBlock) {
                if(!world.isClient) {
                    ItemStack shulkerCopy = new ItemStack(Items.SHULKER_BOX, 1);
                    if (itemStack.hasTag()) {
                        shulkerCopy.setTag(itemStack.getTag().copy());
                    }

                    player.setStackInHand(hand, shulkerCopy);
                    entity.takeBottle(false);
                    player.incrementStat(Stats.CLEAN_SHULKER_BOX);
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.method_29236(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
