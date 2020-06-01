package me.crupette.cauldronoverhaul.transformer;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CauldronBlockEntityTransformer {
    private static final List<Entry> transformers = new ArrayList<>();

    public static void addTransformer(Entry entry){
        transformers.add(entry);
    }

    public static void onTick(CauldronBlockEntity blockEntity, World world, BlockPos pos){
        for(Entry entry : transformers){
            if(entry.onTick(blockEntity, world, pos)) return;
        }
    }

    public interface Entry {
        /*  Hook for CauldronBlockEntity tick()
        *   return: whether execution should stop
        * */
        default boolean onTick(CauldronBlockEntity blockEntity, World world, BlockPos pos){
            return false;
        }
    }
}
