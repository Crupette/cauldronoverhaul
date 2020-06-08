package me.crupette.cauldronoverhaul.block;

import me.crupette.cauldronoverhaul.CauldronOverhaul;
import me.crupette.cauldronoverhaul.actions.CauldronActions;
import me.crupette.cauldronoverhaul.actions.ICauldronAction;
import me.crupette.cauldronoverhaul.transformer.CauldronBlockEntityTransformer;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
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
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CauldronBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    public Fluid fluid;
    public int level_numerator;
    public int level_denominator;

    public boolean dyed;
    public int dyeColor;

    public ItemStack ingredient;
    public int timeLeft;
    public int brewTimeLeft;

    public CauldronBlockEntity() {
        super(CauldronOverhaul.CAULDRON_BLOCK_ENTITY);

        this.fluid = Fluids.EMPTY;
        this.level_numerator = 0;
        this.dyed = false;
        this.dyeColor = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER)
                .getFluidColor(null, null, Fluids.WATER.getDefaultState());
        this.level_denominator = 1;
        this.ingredient = ItemStack.EMPTY;
        this.timeLeft = 0;
        this.brewTimeLeft = 0;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        this.level_numerator = compoundTag.getInt("numerator");
        this.level_denominator = compoundTag.getInt("denominator");

        if(this.level_numerator > this.level_denominator) this.level_numerator = this.level_denominator;

        this.fluid = Registry.FLUID.get(new Identifier(compoundTag.getString("fluid")));

        this.dyed = compoundTag.getBoolean("dyed");
        this.dyeColor = compoundTag.getInt("dyeColor");

        this.ingredient = ItemStack.fromTag(compoundTag.getCompound("ingredient"));
        this.timeLeft = compoundTag.getInt("timeLeft");
        this.brewTimeLeft = compoundTag.getInt("brewTimeLeft");
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        super.fromTag(compoundTag);
        this.level_numerator = compoundTag.getInt("numerator");
        this.level_denominator = compoundTag.getInt("denominator");

        if(this.level_numerator > this.level_denominator) this.level_numerator = this.level_denominator;

        this.fluid = Registry.FLUID.get(new Identifier(compoundTag.getString("fluid")));

        this.dyed = compoundTag.getBoolean("dyed");
        this.dyeColor = compoundTag.getInt("dyeColor");

        this.ingredient = ItemStack.fromTag(compoundTag.getCompound("ingredient"));
        this.timeLeft = compoundTag.getInt("timeLeft");
        this.brewTimeLeft = compoundTag.getInt("brewTimeLeft");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putInt("numerator", this.level_numerator);
        compoundTag.putInt("denominator", this.level_denominator);
        compoundTag.putString("fluid", Registry.FLUID.getId(this.fluid).toString());
        compoundTag.putBoolean("dyed", this.dyed);
        compoundTag.putInt("dyeColor", this.dyeColor);

        CompoundTag ingredientTag = new CompoundTag();
        this.ingredient.toTag(ingredientTag);
        compoundTag.put("ingredient", ingredientTag);
        compoundTag.putInt("timeLeft", this.timeLeft);
        compoundTag.putInt("brewTimeLeft", this.brewTimeLeft);
        return compoundTag;
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag){
        super.toTag(compoundTag);
        compoundTag.putInt("numerator", this.level_numerator);
        compoundTag.putInt("denominator", this.level_denominator);
        compoundTag.putString("fluid", Registry.FLUID.getId(this.fluid).toString());
        compoundTag.putBoolean("dyed", this.dyed);
        compoundTag.putInt("dyeColor", this.dyeColor);

        CompoundTag ingredientTag = new CompoundTag();
        this.ingredient.toTag(ingredientTag);
        compoundTag.put("ingredient", ingredientTag);
        compoundTag.putInt("timeLeft", this.timeLeft);
        compoundTag.putInt("brewTimeLeft", this.brewTimeLeft);
        return compoundTag;
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
        this.level_numerator = level;
        if(this.level_numerator == 0){
            this.fluid = Fluids.EMPTY;
        }
    }

    private void findNearestCommon(int denom){
        int newdenom = 0;
        if(level_denominator % denom == 0) { newdenom = level_denominator; } else
        if(denom % level_denominator == 0) { newdenom = denom; } else
            { newdenom = level_denominator * denom; }

        int mult = newdenom / level_denominator;
        this.level_numerator *= mult;
        this.level_denominator = newdenom;
    }

    public boolean drain(int numerator, int denominator, boolean simulate){
        findNearestCommon(denominator);
        if(denominator != this.level_denominator){
            numerator *= ((double) this.level_denominator / denominator);
        }
        if(this.level_numerator < numerator) return false;
        if(!simulate) {
            this.level_numerator -= numerator;
            if (this.level_numerator == 0) {
                this.fluid = Fluids.EMPTY;
                this.dyed = false;
                this.dyeColor = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER)
                        .getFluidColor(null, null, Fluids.WATER.getDefaultState());
            }
        }
        return true;
    }

    public boolean fill(int numerator, int denominator, Fluid fluid, boolean simulate){
        findNearestCommon(denominator);
        if(denominator != this.level_denominator){
            numerator *= ((double) this.level_denominator / denominator);
        }
        if(this.fluid != fluid && this.fluid != Fluids.EMPTY) return false;
        if(this.level_denominator < this.level_numerator + numerator) return false;
        if(!simulate) {
            this.level_numerator += numerator;
            this.fluid = fluid;
        }
        return true;
    }

    public boolean takeBottle(boolean simulate){
        return this.drain(1, 3, simulate);
    }

    public boolean insertBottle(Fluid fluid, boolean simulate) {
        return this.fill(1, 3, fluid, simulate);
    }

    @Override
    public void tick() {
        CauldronBlockEntityTransformer.onTick(this, this.world, this.pos);
    }
}
