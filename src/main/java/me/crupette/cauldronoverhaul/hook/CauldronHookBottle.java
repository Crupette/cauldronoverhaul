package me.crupette.cauldronoverhaul.hook;

import me.crupette.cauldronoverhaul.api.CauldronHook;
import me.crupette.cauldronoverhaul.api.FluidVolume;
import me.crupette.cauldronoverhaul.api.HookTransformer;
import me.crupette.cauldronoverhaul.block.entity.CauldronBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
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

public class CauldronHookBottle implements CauldronHook {

    @Override
    public ActionResult onUse(CauldronBlockEntity blockEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if(item instanceof PotionItem){
            Potion potion = PotionUtil.getPotion(itemStack);

            if(potion.equals(Potions.WATER)){
                FluidVolume insert = new FluidVolume(Fluids.WATER, Fraction.ONE_THIRD);
                FluidVolume result = blockEntity.getSlot(0).insert(insert, false);
                if(result.getCount().floatValue() == insert.getCount().floatValue()) return ActionResult.PASS;
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.f, 1.f);
                if(!player.isCreative()){
                    player.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                }
                blockEntity.markDirty();
                if(!world.isClient) blockEntity.sync();
                return ActionResult.success(world.isClient);
            }
        }else if(item == Items.GLASS_BOTTLE){
            FluidVolume extract = new FluidVolume(Fluids.WATER, Fraction.ONE_THIRD);
            if(blockEntity.getSlot(0).extract(extract, true) != extract) return ActionResult.PASS;
            blockEntity.getSlot(0).extract(extract, false);
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.f, 1.f);
            if(!player.isCreative()){
                player.setStackInHand(hand, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
            }
            blockEntity.markDirty();
            if(!world.isClient) blockEntity.sync();
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
}
