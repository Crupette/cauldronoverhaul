package me.crupette.cauldronoverhaul.actions;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronActionBottle implements ICauldronAction{
    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof PotionItem){
            if(PotionUtil.getPotion(itemStack) == Potions.WATER && entity.insertBottle(Fluids.WATER, false)){
                if(!world.isClient){
                    if(!player.abilities.creativeMode){
                        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);
                        player.incrementStat(Stats.USE_CAULDRON);
                        player.setStackInHand(hand, glassBottle);
                        if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)player).openHandledScreen(player.playerScreenHandler);
                        }
                    }
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.success(world.isClient);
            }
        }
        else if(itemStack.getItem() == Items.GLASS_BOTTLE){
            if(entity.fluid == Fluids.WATER && entity.takeBottle(true)){
                if(!world.isClient){
                    if(!player.abilities.creativeMode){
                        ItemStack waterBottleItem = PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                        player.incrementStat(Stats.USE_CAULDRON);
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            player.setStackInHand(hand, waterBottleItem);
                        } else if (!player.inventory.insertStack(waterBottleItem)) {
                            player.dropItem(waterBottleItem, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)player).openHandledScreen(player.playerScreenHandler);
                        }
                    }
                    entity.takeBottle(false);
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
