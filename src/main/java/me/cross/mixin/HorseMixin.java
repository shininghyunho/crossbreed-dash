package me.cross.mixin;

import me.cross.entity.HorseAbility;
import me.cross.handler.CheckPointBlockHandler;
import me.cross.handler.HorseOwnerHandler;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.handler.RacingHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseMixin extends Entity {
    public HorseMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private HorseAbility horseAbility;

    // getSaddledSpeed : 말에 탑승하면 tick 당 호출됨
    @Inject(at = @At("TAIL"), method = "getSaddledSpeed", cancellable = true)
    private void getSaddledSpeed(PlayerEntity controllingPlayer,CallbackInfoReturnable<Float> cir) {
        int x = (int) this.getX(), z = (int) this.getZ();
        // 말이 움직일 수 없는 상태라면 속도를 0으로 설정
        if(isHorseMoveable()) cir.setReturnValue(0.0F);
        // 속도를 n배로 증가시킴
        else if(horseAbility!=null) cir.setReturnValue(cir.getReturnValue() * horseAbility.speedMultiplier);
    }

    // jump
    @ModifyVariable(at = @At("STORE"), method = "jump", ordinal = 0)
    private double getD(double original) {
        // 점프력을 n배로 증가시킴
        return horseAbility!=null ? original * horseAbility.jumpMultiplier : original;
    }

    // isTame
    @Inject(at = @At("TAIL"), method = "isTame", cancellable = true)
    private void isTame(CallbackInfoReturnable<Boolean> cir) {
        // 말이 항상 길들여져있는 상태로 설정
        cir.setReturnValue(true);
    }

    // putPlayerOnBack
    @Inject(at = @At("TAIL"), method = "putPlayerOnBack", cancellable = true)
    private void putPlayerOnBack(PlayerEntity player, CallbackInfo ci) {
        ActionResult result = HorseBondWithPlayerCallback.EVENT.invoker().interact(player, (AbstractHorseEntity) (Object) this);
        if(result != ActionResult.PASS) {
            ci.cancel();
        }

        // set horseAbility
        if(horseAbility==null) horseAbility = HorseOwnerHandler.getHorseAbility(player.getUuid(), getUuid());
    }

    // canJump
    @Inject(at = @At("TAIL"), method = "canJump", cancellable = true)
    private void canJump(CallbackInfoReturnable<Boolean> cir) {
        // 말이 점프할 수 없는 상태라면 점프를 막음
        if(isHorseMoveable()) cir.setReturnValue(false);
    }

    // isSaddled
    @Inject(at = @At("TAIL"), method = "isSaddled", cancellable = true)
    private void isSaddled(CallbackInfoReturnable<Boolean> cir) {
        // 말이 항상 안장을 타고 있는 상태로 설정
        cir.setReturnValue(true);
    }

    @Unique
    private boolean isHorseMoveable() {
        int x = (int) this.getX(), z = (int) this.getZ();
        return !RacingHandler.isReadyMode() || CheckPointBlockHandler.isPlayerAtStartPoint(x,z);
    }
}
