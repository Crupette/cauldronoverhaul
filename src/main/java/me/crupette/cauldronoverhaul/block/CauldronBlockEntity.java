package me.crupette.cauldronoverhaul.block;

import me.crupette.cauldronoverhaul.CauldronOverhaul;
import me.crupette.cauldronoverhaul.actions.CauldronActions;
import me.crupette.cauldronoverhaul.actions.ICauldronAction;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CauldronBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public Fluid fluid;
    public int level;
    public int internal_bottleCount;

    public CauldronBlockEntity() {
        super(CauldronOverhaul.CAULDRON_BLOCK_ENTITY);

        this.fluid = Fluids.EMPTY;
        this.level = 0;
        this.internal_bottleCount = 0;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        this.level = compoundTag.getInt("level");
        this.internal_bottleCount = this.level / 333;
        this.fluid = Registry.FLUID.get(new Identifier(compoundTag.getString("fluid")));
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.level = tag.getInt("level");
        this.internal_bottleCount = this.level / 333;
        this.fluid = Registry.FLUID.get(new Identifier(tag.getString("fluid")));
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putInt("level", this.level);
        compoundTag.putString("fluid", Registry.FLUID.getId(this.fluid).toString());
        return compoundTag;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag){
        tag = super.toTag(tag);
        tag.putInt("level", this.level);
        tag.putString("fluid", Registry.FLUID.getId(this.fluid).toString());
        return tag;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit, ItemStack itemStack){
        for(ICauldronAction action : CauldronActions.getCauldronActions()){
            ActionResult result = action.onUse(this, world, pos, player, hand);
            if(!result.equals(ActionResult.PASS)) {
                if(!world.isClient) this.sync();
                return result;
            }
        }
        return ActionResult.PASS;
    }

    public void setLevel(int level){
        this.internal_bottleCount = level / 333;
        this.level = level;
        this.markDirty();
    }

    public void takeBottle(){
        this.internal_bottleCount--;
        this.level -= 333;
        if(this.internal_bottleCount == 0){
            this.level = 0;
            this.fluid = Fluids.EMPTY;
        }
        this.markDirty();
    }

    public void insertBottle(){
        this.internal_bottleCount++;
        this.level += 333;
        if(this.internal_bottleCount == 3){
            this.level = 1000;
        }
        this.markDirty();
    }
}
