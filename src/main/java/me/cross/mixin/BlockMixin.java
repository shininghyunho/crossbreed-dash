package me.cross.mixin;

import me.cross.Cross;
import me.cross.handler.race.CheckPointBlockHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    // onplaced
    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if(world.isClient) return;

        Block block = (Block) (Object) this;
        if(CheckPointBlockHandler.isCheckpoint(block)) {
            CheckPointBlockHandler.addCheckPointPos(block, pos);
            Cross.LOGGER.info("체크 포인트 블록 등록! : " + block.getTranslationKey());
        }
    }

    // onBreak
    @Inject(method = "onBreak", at = @At("TAIL"))
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if(world.isClient) return;

        Block block = (Block) (Object) this;
        if(CheckPointBlockHandler.isCheckpoint(block)) {
            CheckPointBlockHandler.removeCheckPointPos(block, pos);
            Cross.LOGGER.info("체크 포인트 블록 제거! : " + block.getTranslationKey());
        }
    }
}
