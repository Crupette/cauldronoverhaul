package me.crupette.cauldronoverhaul.integration.potiontipped.actions;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.item.TippedTool;
import com.github.Crupette.potiontipped.util.TippedItemUtil;
import me.crupette.cauldronoverhaul.actions.ICauldronAction;
import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.fluidpotions.fluid.PotionFluid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

//Basic tool "dipping" to provide adequate integration with potiontipped
public class CauldronActionDip implements ICauldronAction {
    @Override
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getStackInHand(hand);
        Item heldItem = heldStack.getItem();

        //Cauldron contains a fluid, and has more than 1/3 of that fluid
        if(entity.fluid instanceof PotionFluid && entity.takeBottle(true)){

            //Held item is a tool
            if(heldItem instanceof MiningToolItem || heldItem instanceof SwordItem){
                TippedItemUtil.TippedType type = TippedItemUtil.TippedType.HEAD;
                if(heldItem instanceof TippedTool){
                    //Replaces the held item with the parent item, so the conversion doesn't mess up and make air
                    heldItem = ((TippedTool)heldItem).getParent();  //This works :)
                    Potion handlePotion = PotionUtil.getPotion(heldStack.getOrCreateSubTag("handle"));
                    if(handlePotion != Potions.EMPTY) type = TippedItemUtil.getTippedType(type.getValue() | TippedItemUtil.TippedType.HANDLE.getValue());
                }
                Identifier id = Registry.ITEM.getId(heldItem);

                //Build new itemstack based on the held tool, and give it the appropriate type
                ItemStack tippedStack = new ItemStack(PotionTipped.TIPPED_TOOLS.get(
                        new Identifier(id.getNamespace(), id.getPath() + "-" + TippedItemUtil.getSuffixFromType(type))));

                //Copy over NBT data (like damage)
                tippedStack.setTag(heldStack.getTag());

                //Overwrite previous head potion with new potion
                tippedStack.getOrCreateSubTag("head").putString("Potion", Registry.POTION.getId(((PotionFluid)entity.fluid).getPotion()).toString());

                //Keep things server sided.
                if(!world.isClient){
                    player.setStackInHand(hand, tippedStack);
                    world.playSound(player, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.f, 1.f);
                    player.incrementStat(Stats.USE_CAULDRON);
                    entity.takeBottle(false);
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
