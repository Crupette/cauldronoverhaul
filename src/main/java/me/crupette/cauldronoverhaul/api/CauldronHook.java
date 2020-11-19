package me.crupette.cauldronoverhaul.api;

import me.crupette.cauldronoverhaul.block.entity.CauldronBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface CauldronHook {

    default void onEntityCollision(CauldronBlockEntity blockEntity,
                                   BlockState state, World world, BlockPos pos, Entity entity)
    {}

    default int getComparatorOutput(CauldronBlockEntity blockEntity,
                                    BlockState state, World world, BlockPos pos)
    { return 0; }

    default boolean canPathfindThrough(CauldronBlockEntity blockEntity,
                                       BlockState state, BlockView world, BlockPos pos, NavigationType type)
    { return false; }

    default ActionResult onUse(CauldronBlockEntity blockEntity,
                               BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    { return ActionResult.PASS; }

    default void rainTick(CauldronBlockEntity blockEntity,
                          World world, BlockPos pos)
    {}

    default CompoundTag toTag(CauldronBlockEntity blockEntity,
                              CompoundTag tag)
    { return tag; }

    default void fromTag(CauldronBlockEntity blockEntity,
                         CompoundTag tag)
    {}
}
