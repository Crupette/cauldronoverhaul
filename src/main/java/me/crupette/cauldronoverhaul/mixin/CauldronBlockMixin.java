package me.crupette.cauldronoverhaul.mixin;

import me.crupette.cauldronoverhaul.block.CauldronBlockEntity;
import me.crupette.cauldronoverhaul.util.CauldronBlockTransformer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends Block implements BlockEntityProvider {

    public CauldronBlockMixin(Settings settings) {
        super(settings);
    }

    //I do this right here so that mods that do special cauldron interactions can still work if they inject at HEAD
    @Inject(method = "onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit,
                      CallbackInfoReturnable<ActionResult> ci, ItemStack itemStack, int i, Item item) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof CauldronBlockEntity){
            ci.setReturnValue(((CauldronBlockEntity) entity).onUse(state, world, pos, player, hand, hit, itemStack));
            return;
        }
        ci.setReturnValue(ActionResult.PASS);
    }

    //I'm sorry
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(!(world.getBlockEntity(pos) instanceof CauldronBlockEntity))
            return;
        CauldronBlockEntity blockEntity = (CauldronBlockEntity)world.getBlockEntity(pos);
        if(blockEntity.fluid == Fluids.EMPTY)
            return;
        float fluidHeight = pos.getY() + (0.25f) + ((0.7f) * (blockEntity.level / 1000.f));
        if (!world.isClient && entity.getY() <= (double)fluidHeight) {
            if(CauldronBlockTransformer.onEntityCollision(state, world, pos, entity, blockEntity)) return;
            blockEntity.fluid.getDefaultState().getBlockState().onEntityCollision(world, pos, entity);
            if(blockEntity.fluid == Fluids.WATER && entity.isOnFire()){
                entity.extinguish();
            }
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view){
        return new CauldronBlockEntity();
    }
}
