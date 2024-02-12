package me.cross.mixin;

import me.cross.handler.HorseOwnerHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin {
    // player 가 소유한 말의 능력치를 저장
    // writeCustomDataToNbt
    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        // HorseOwnerHandler.horseAbilitiesMap 을 nbt 에 저장
        HorseOwnerHandler.writeToNbt(nbt);
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        // nbt 에서 HorseOwnerHandler.horseAbilitiesMap 을 읽어옴
        HorseOwnerHandler.readFromNbt(nbt);
    }
}
