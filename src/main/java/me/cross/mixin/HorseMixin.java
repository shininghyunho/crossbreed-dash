package me.cross.mixin;

import me.cross.Cross;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.entity.HorseAbility;
import me.cross.handler.CheckPointBlockHandler;
import me.cross.handler.HorseOwnerHandler;
import me.cross.handler.RacingHandler;
import me.cross.handler.RunningHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
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
    private static final double DEFAULT_SPEED=0.2d;
    public HorseMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // getSaddledSpeed : 말에 탑승하면 tick 당 호출됨
    @Inject(at = @At("TAIL"), method = "getSaddledSpeed", cancellable = true)
    private void getSaddledSpeed(PlayerEntity controllingPlayer,CallbackInfoReturnable<Float> cir) {
        HorseAbility horseAbility = getHorseAbility();
        // 말이 움직일 수 없는 상태라면 속도를 0으로 설정
        if(isHorseNotMoveable()) cir.setReturnValue(0.0F);
        // 속도를 horseAbility.speedMultiplier 배로 증가시킴
        else if(horseAbility!=null) {
            cir.setReturnValue((float) ((float) DEFAULT_SPEED * horseAbility.speedMultiplier));
        }
    }

    // jump
    @ModifyVariable(at = @At("STORE"), method = "jump", ordinal = 0)
    private double getD(double original) {
        // 주인이 없는 말이면 return
        if(!isHaveOwner()) return original;

        // 점프력을 n배로 증가시킴
        HorseAbility horseAbility = getHorseAbility();
        return horseAbility==null? original : original * horseAbility.jumpMultiplier;
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
        // add horse ability event
        HorseBondWithPlayerCallback.EVENT.invoker().interact(player, (AbstractHorseEntity) (Object) this);

        if(!isHaveOwner()) {
            AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
            horse.bondWithPlayer(player);
            setHorseNameTag();
            setImmortal();
        }
    }

    @Inject(at = @At("TAIL"), method = "canJump", cancellable = true)
    private void canJump(CallbackInfoReturnable<Boolean> cir) {
        // 주인이 없는 말이면 return
        if(!isHaveOwner()) return;

        // 말이 점프할 수 없는 상태라면 점프를 막음
        if(isHorseNotMoveable()) cir.setReturnValue(false);
    }
    // isSaddled
    @Inject(at = @At("TAIL"), method = "isSaddled", cancellable = true)
    private void isSaddled(CallbackInfoReturnable<Boolean> cir) {
        // 말이 항상 안장을 타고 있는 상태로 설정
        cir.setReturnValue(true);
    }
    // every tick

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        // 러닝 모드일때 체크포인트 지나갔는지 확인
        if(RacingHandler.isRunning() && !getWorld().isClient && !isHaveOwner()) {
            int x = (int) getX(), z = (int) getZ(), idx = RunningHandler.getCheckpointIdx(getUuid());
            if(idx==-1) return;

            // 체크포인트 지나갔는지 확인
            if(CheckPointBlockHandler.isPlayerAtIdxPoint(x,z,idx) && RunningHandler.isPassedInorder(getUuid(),idx)) {
                RunningHandler.setPassed(getUuid(),idx);
                RunningHandler.setNextCheckpointIdx(getUuid());
            }
            // 1바퀴 돌았는지 확인
            if(RunningHandler.isLapFinished(getUuid())) {
                RunningHandler.setNextLap(getUuid());
            }
        }
    }

    @Unique
    private boolean isHorseNotMoveable() {
        int x = (int) getX(), z = (int) getZ();
        return (RacingHandler.isReadyForRunning() || RacingHandler.isCountdown()) && CheckPointBlockHandler.isPlayerAtIdxPoint(x,z,0);
    }
    
    @Unique
    private HorseAbility getHorseAbility() {
        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
        if(horse.getOwnerUuid() == null) return null;
        return HorseOwnerHandler.getHorseAbility(horse.getOwnerUuid(), getUuid());
    }

    @Unique
    private void setHorseNameTag() {
        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
        if(horse.getOwnerUuid() == null) return;

        // get horse name
        HorseAbility horseAbility = getHorseAbility();
        if(horseAbility == null) return;
        String horseName = horseAbility.name;

        // add to name tag
        ItemStack nameTagStack = new ItemStack(Items.NAME_TAG);
        nameTagStack.setCustomName(Text.of(horseName));

        // add name tag to horse
        horse.setCustomName(nameTagStack.getName());

        Cross.LOGGER.info("말 이름 태그 설정 : " + horseAbility.name);
    }

    @Unique
    private boolean isHaveOwner() {
        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
        return horse.getOwnerUuid() != null;
    }

    @Unique
    private void setImmortal () {
        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
        horse.setInvulnerable(true);
    }
}
