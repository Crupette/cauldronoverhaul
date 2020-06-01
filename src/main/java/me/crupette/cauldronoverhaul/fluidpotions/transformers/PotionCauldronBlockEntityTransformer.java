package me.crupette.cauldronoverhaul.fluidpotions.transformers;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.transformer.CauldronBlockEntityTransformer;
import me.crupette.fluidpotions.FluidPotions;
import me.crupette.fluidpotions.fluid.PotionFluid;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotionCauldronBlockEntityTransformer implements CauldronBlockEntityTransformer.Entry {

    public static List<Block> hotBlocks = new ArrayList<>(Arrays.asList(
            Blocks.LAVA, Blocks.FIRE, Blocks.CAMPFIRE
    ));

    public boolean onTick(CauldronBlockEntity blockEntity, World world, BlockPos pos) {
        if(blockEntity.fluid == Fluids.EMPTY && blockEntity.ingredient != ItemStack.EMPTY){
            blockEntity.ingredient = ItemStack.EMPTY;
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.F, 1.F);
            if(!world.isClient) blockEntity.sync();
            return false;
        }
        if(blockEntity.timeLeft > 0 && !world.isClient){
            blockEntity.timeLeft--;
            if(blockEntity.timeLeft == 0){
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.F, 1.F);
                blockEntity.sync();
            }

            if(!hotBlocks.contains(world.getBlockState(pos.down()).getBlock())){
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.F, 1.F);
                blockEntity.timeLeft = 0;
                blockEntity.ingredient = ItemStack.EMPTY;
                blockEntity.sync();
                return false;
            }
            //Fluid in cauldron is brewable
            if(blockEntity.fluid.matchesType(Fluids.WATER) ||
                    (blockEntity.fluid instanceof PotionFluid)){
                ItemStack baseStack = PotionUtil.setPotion(new ItemStack(Items.POTION),
                        blockEntity.fluid.matchesType(Fluids.WATER) ? Potions.WATER : ((PotionFluid)blockEntity.fluid).getPotion());
                //Potion can be brewed using this ingredient
                if(BrewingRecipeRegistry.hasRecipe(baseStack, blockEntity.ingredient)){
                    Item ingredientItem = blockEntity.ingredient.getItem();
                    blockEntity.brewTimeLeft--;
                    //Brewing is done (timer is stored in ingredient count, hope this doesn't bite me in the ass
                    if(blockEntity.brewTimeLeft <= 0){
                        ItemStack result = BrewingRecipeRegistry.craft(new ItemStack(ingredientItem), baseStack);
                        blockEntity.fluid = FluidPotions.INSTANCE.getStill(PotionUtil.getPotion(result));
                        blockEntity.ingredient = ItemStack.EMPTY;
                        blockEntity.sync();
                        world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.F, 1.F);
                    }
                }
                blockEntity.sync();
            }
        }
        return false;
    }
}
