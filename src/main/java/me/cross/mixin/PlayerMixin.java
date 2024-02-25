package me.cross.mixin;

import me.cross.Cross;
import me.cross.handler.race.CheckPointBlockHandler;
import me.cross.handler.horse.HorseAbilityHandler;
import me.cross.handler.horse.HorseNameHandler;
import me.cross.handler.race.RacingHandler;
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
        HorseAbilityHandler.writeToNbt(nbt);
        CheckPointBlockHandler.writeToNbt(nbt);
        HorseNameHandler.writeToNbt(nbt);

        // 안죽게 설정
        setImmortal();
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        // nbt 에서 HorseOwnerHandler.horseAbilitiesMap 을 읽어옴
        Cross.LOGGER.info("readCustomDataFromNbt");
        HorseAbilityHandler.readFromNbt(nbt);
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

    // 안죽게
    private void setImmortal() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        player.setInvulnerable(true);
    }
}
