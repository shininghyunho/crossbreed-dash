package me.cross.mixin;

import me.cross.entity.HorseAbility;
import me.cross.handler.HorseOwnerHandler;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
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
        // 속도를 n배로 증가시킴
        if(horseAbility!=null) cir.setReturnValue(cir.getReturnValue() * horseAbility.speedMultiplier);
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
}
