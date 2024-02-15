package me.cross.mixin;

import me.cross.Cross;
import me.cross.handler.CheckPointBlockHandler;
import me.cross.handler.HorseNameHandler;
import me.cross.handler.HorseOwnerHandler;
import me.cross.handler.RacingHandler;
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
        Cross.LOGGER.info("writeCustomDataToNbt");
        HorseOwnerHandler.writeToNbt(nbt);
        CheckPointBlockHandler.writeToNbt(nbt);
        HorseNameHandler.writeToNbt(nbt);
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        // nbt 에서 HorseOwnerHandler.horseAbilitiesMap 을 읽어옴
        Cross.LOGGER.info("readCustomDataFromNbt");
        HorseOwnerHandler.readFromNbt(nbt);
        CheckPointBlockHandler.readFromNbt(nbt);
        HorseNameHandler.readFromNbt(nbt);
    }

    // 움직이지 못할때는 내리지 못함
    // dismountVehicle
    @Inject(at = @At("HEAD"), method = "dismountVehicle", cancellable = true)
    private void dismountVehicle(CallbackInfo ci) {
        // 카운트 다운 or 레이싱 중일때 내리지 못함.
        if(RacingHandler.isCountdown() || RacingHandler.isRunning()) ci.cancel();
    }
}
