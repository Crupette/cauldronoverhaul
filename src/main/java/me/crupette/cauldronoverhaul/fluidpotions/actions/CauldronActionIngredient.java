package me.crupette.cauldronoverhaul.fluidpotions.actions;

import me.crupette.cauldronoverhaul.actions.ICauldronAction;
import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.fluidpotions.fluid.PotionFluid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronActionIngredient implements ICauldronAction {
    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);
        if(entity.fluid == Fluids.EMPTY) return ActionResult.PASS;
        if(BrewingRecipeRegistry.hasRecipe(
                PotionUtil.setPotion(new ItemStack(Items.POTION), entity.fluid.matchesType(Fluids.WATER) ? Potions.WATER :
                        (entity.fluid instanceof PotionFluid ? ((PotionFluid)entity.fluid).getPotion() : Potions.EMPTY)),
                heldItem)){
            if(entity.ingredient.isEmpty()){
                if(!world.isClient){
                    Item ingredient = heldItem.getItem();
                    if(!player.abilities.creativeMode){
                        heldItem.decrement(1);
                    }
                    entity.ingredient = new ItemStack(ingredient, 1);
                    entity.brewTimeLeft = 200;
                    entity.sync();
                }
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.f, 1.f);
                return ActionResult.method_29236(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
