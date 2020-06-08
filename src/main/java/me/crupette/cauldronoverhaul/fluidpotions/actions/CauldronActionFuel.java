package me.crupette.cauldronoverhaul.fluidpotions.actions;

import me.crupette.cauldronoverhaul.actions.ICauldronAction;
import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronActionFuel implements ICauldronAction {
    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);
        if (heldItem.getItem() == Items.BLAZE_POWDER) {
            if (!world.isClient) {
                if (!player.abilities.creativeMode) {
                    heldItem.decrement(1);
                }
                entity.timeLeft += 1000;
                world.playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.f, 1.f);
                entity.sync();
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
}
