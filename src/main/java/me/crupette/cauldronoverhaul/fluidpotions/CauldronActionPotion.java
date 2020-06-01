package me.crupette.cauldronoverhaul.fluidpotions;

import me.crupette.cauldronoverhaul.actions.ICauldronAction;
import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.fluidpotions.FluidPotions;
import me.crupette.fluidpotions.fluid.PotionFluid;
import me.crupette.fluidpotions.item.PotionBucketItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
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

public class CauldronActionPotion implements ICauldronAction {
    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);
        if(heldItem.getItem() instanceof PotionBucketItem) {
            Fluid fluid = FluidPotions.INSTANCE.getStill(PotionUtil.getPotion(heldItem));
            if (entity.level < 1000 && (entity.fluid == Fluids.EMPTY || fluid == entity.fluid) && !world.isClient) {

                if (!player.abilities.creativeMode) {
                    player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                }
                player.incrementStat(Stats.FILL_CAULDRON);
                entity.fluid = fluid;
                entity.setLevel(1000);
                entity.sync();
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }else if(heldItem.getItem() instanceof PotionItem){
            Potion potion = PotionUtil.getPotion(heldItem);
            PotionFluid fluid = FluidPotions.INSTANCE.getStill(potion);
            if(entity.internal_bottleCount < 3 && (entity.fluid == Fluids.EMPTY || fluid == entity.fluid) && !world.isClient){
                if(!player.abilities.creativeMode){
                    ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.setStackInHand(hand, glassBottle);
                    if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)player).openContainer(player.playerContainer);
                    }
                }
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                entity.fluid = fluid;
                entity.insertBottle();
                return ActionResult.SUCCESS;
            }
        }else if(heldItem.getItem() == Items.GLASS_BOTTLE){
            if(entity.internal_bottleCount > 0 && entity.fluid instanceof PotionFluid && !world.isClient){
                if(!player.abilities.creativeMode){
                    ItemStack potionItem = PotionUtil.setPotion(new ItemStack(Items.POTION), ((PotionFluid)entity.fluid).getPotion());
                    player.incrementStat(Stats.USE_CAULDRON);
                    heldItem.decrement(1);
                    if (heldItem.isEmpty()) {
                        player.setStackInHand(hand, potionItem);
                    } else if (!player.inventory.insertStack(potionItem)) {
                        player.dropItem(potionItem, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)player).openContainer(player.playerContainer);
                    }
                }
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                entity.takeBottle();
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
