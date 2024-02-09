package me.cross.mixin;

import me.cross.Cross;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin {
    // 드랍하고 다시 주움
    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",at = @At("TAIL"),cancellable = true)
    public void dropAndGet(@NotNull ItemStack stack, boolean throwRandomly, boolean retainOwnership, @NotNull CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        Cross.LOGGER.info("Player " + player.getDisplayName() + " dropped " + stack.getItem().getName());

        // when drop
        double d = player.getEyeY() - 0.30000001192092896;
        ItemEntity itemEntity = new ItemEntity(player.getWorld(), player.getX(), d, player.getZ(), stack);

        // return value
        Cross.LOGGER.info(String.valueOf(cir.getReturnValue()));
        // 리턴 값을 던지는 아이템으로 변경
        cir.setReturnValue(itemEntity);
    }
}
