package me.crupette.cauldronoverhaul.block.entity;

import me.crupette.cauldronoverhaul.api.CauldronHook;
import me.crupette.cauldronoverhaul.api.FluidVolume;
import me.crupette.cauldronoverhaul.api.Tank;
import me.crupette.cauldronoverhaul.api.TankImpl;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CauldronBlockEntity extends BlockEntity implements TankImpl, Tickable, BlockEntityClientSerializable {

    public static List<CauldronHook> hooks = new ArrayList<>();
    private DefaultedList<FluidSlot> volumes;

    public CauldronBlockEntity(int level){
        super(COBlockEntities.CAULDRON_BLOCK_ENTITY);

        Fluid fluid = Fluids.WATER;
        if(level == 0) fluid = Fluids.EMPTY;

        this.volumes = DefaultedList.ofSize(1, new FluidSlot(new FluidVolume(fluid, Fraction.getFraction(level, 3)), Fraction.ONE));
    }

    public CauldronBlockEntity() {
        this(0);
    }

    @Override
    public DefaultedList<FluidSlot> getVolumes() {
        return this.volumes;
    }

    @Override
    public void tick() {

    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        for(CauldronHook hook : hooks){
            ActionResult result = hook.onUse(this, state, world, pos, player, hand, hit);
            if(result != ActionResult.PASS) return result;
        }
        return ActionResult.PASS;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Tank.fromTag(this, tag);
        hooks.forEach(hook -> {
            hook.fromTag(this, tag);
        });

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Tank.toTag(this, tag);
        hooks.forEach(hook -> {
            hook.toTag(this, tag);
        });
        return tag;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        Tank.fromTag(this, tag);
        hooks.forEach(hook -> {
            hook.fromTag(this, tag);
        });
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        Tank.toTag(this, tag);
        hooks.forEach(hook -> {
            hook.toTag(this, tag);
        });
        return tag;
    }

    public void onCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        hooks.forEach(hook -> {
            hook.onEntityCollision(this, state, world, pos, entity);
        });
    }
}
