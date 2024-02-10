package me.cross.mixin;

import me.cross.Cross;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseMixin extends Entity {
    public HorseMixin(EntityType<?> type, World world) {
        super(type, world);
        controllingPlayer = getOwner();
    }

    @Shadow
    private UUID ownerUuid;

    @Unique
    private static final float SPEED_FACTOR = 10f;
    @Unique
    private float speedMultiplier;
    @Unique
    private final PlayerEntity controllingPlayer;

    // getSaddledSpeed : 말에 탑승하면 tick 당 호출됨
    @Inject(at = @At("TAIL"), method = "getSaddledSpeed", cancellable = true)
    private void getSaddledSpeed(PlayerEntity controllingPlayer,CallbackInfoReturnable<Float> cir) {
        // 속도를 n배로 증가시킴
        cir.setReturnValue(cir.getReturnValue() * speedMultiplier);
    }

    // isTame
    @Inject(at = @At("TAIL"), method = "isTame", cancellable = true)
    private void isTame(CallbackInfoReturnable<Boolean> cir) {
        // 말이 항상 길들여져있는 상태로 설정
        cir.setReturnValue(true);
    }

    // putPlayerOnBack
    @Inject(at = @At("TAIL"), method = "putPlayerOnBack")
    private void putPlayerOnBack(PlayerEntity player, CallbackInfo ci) {
        // 말에 탑승하면 속도가 랜덤하게 설정됨
        if(getWorld().isClient()) {
            speedMultiplier = this.getRandomSpeed();
            Cross.LOGGER.info("putPlayerOnBack : 말의 속도가 " + speedMultiplier + "배로 설정되었습니다.");
        }
    }

    // ########################## UNIQUE METHODS
    @Unique
    private float getRandomSpeed() {
        return this.random.nextFloat() * SPEED_FACTOR;
    }

    // owner
    @Unique
    private PlayerEntity getOwner() {
        if (this.ownerUuid == null) {
            return null;
        } else {
            return this.getWorld().getPlayerByUuid(this.ownerUuid);
        }
    }
}
