package me.cross.mixin;

import me.cross.Cross;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.entity.HorseAbility;
import me.cross.handler.MessageHandler;
import me.cross.handler.horse.HorseAbilityHandler;
import me.cross.handler.race.CheckPointBlockHandler;
import me.cross.handler.race.RacingHandler;
import me.cross.handler.race.RunningHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseMixin extends Entity {
    @Shadow @Nullable public abstract LivingEntity getControllingPassenger();
    @Unique
    private final AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;

    private static final int MAX_CRAZY_COUNT = 60;
    private static final double DEFAULT_SPEED=0.2d;
    private static int crazyCount = 0;
    public HorseMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // getSaddledSpeed : 말에 탑승하면 tick 당 호출됨
    @Inject(at = @At("TAIL"), method = "getSaddledSpeed", cancellable = true)
    private void getSaddledSpeed(PlayerEntity controllingPlayer,CallbackInfoReturnable<Float> cir) {
        HorseAbility horseAbility = getHorseAbility();
        // 말이 움직일 수 없는 상태라면 속도를 0으로 설정
        if(isHorseNotMoveable()) cir.setReturnValue(0.0F);
        // crazy count > 0 이면 속도를 0으로 설정
        else if(crazyCount > 0) {
            cir.setReturnValue(0.0F);
            crazyCount--;
        }
        else if(isHorseCrazy()) {
            setRandCrazyCount();
            MessageHandler.sendToPlayerWithOverlay(controllingPlayer, "말이 변덕 때문에 움직일 수 없습니다. 잠시 기달주세요.");
        }
        // 속도를 horseAbility.speedMultiplier 배로 증가시킴
        else if(horseAbility!=null) {
            cir.setReturnValue((float) ((float) DEFAULT_SPEED * horseAbility.speedMultiplier));
        }
    }

    // jump
    @ModifyVariable(at = @At("STORE"), method = "jump", ordinal = 0)
    private double getD(double original) {
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
    @Inject(at = @At("HEAD"), method = "putPlayerOnBack", cancellable = true)
    private void putPlayerOnBack(PlayerEntity player, CallbackInfo ci) {
        // 말의 주인과 player 가 같지 않다면 탈 수 없음
        HorseAbility horseAbility = getHorseAbility();
        if(horseAbility != null && !horseAbility.ownerUuid.equals(player.getUuid())) {
            MessageHandler.sendToPlayerWithOverlay(player, "말의 주인만 탈 수 있습니다.");
            ci.cancel();
        }

        // 말에게 능력 부여, horseAbility 가 없다면 생성
        HorseBondWithPlayerCallback.EVENT.invoker().interact(player, horse);
        // 주인이 없는 말이면(말 entity 에 ownerUuid 가 없다면) 주인을 설정
        if(!isHaveOwner()) {
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
        if(RacingHandler.isRunning() && !getWorld().isClient && isHaveOwner() && this.hasPassengers()) {
            PlayerEntity player = (PlayerEntity) this.getControllingPassenger();
            if(player == null) return;
            int x = (int) getX(), z = (int) getZ(), idx = RunningHandler.getCheckpointIdx(player.getUuid());
            if(idx==-1) return;

            // idx : 현재까지 지난 체크포인트
            // idx 가 0이 라면 1번째 체크포인트를 지날때 true
            // 그리고 idx+1 아직 false 일때
            if(CheckPointBlockHandler.isPlayerAtIdxPoint(x,z,idx+1) && RunningHandler.isPassed(player.getUuid(),idx) && !RunningHandler.isPassed(player.getUuid(),idx+1)) {
                Cross.LOGGER.info("체크포인트 " + (idx+1) + "번 pass.");
                RunningHandler.setPassed(player.getUuid(),idx+1);
            }
            // 1바퀴 돌았는지 확인
            if(RunningHandler.isLapFinished(x,z,idx,player.getUuid())) {
                RunningHandler.setNextLap(player.getUuid());
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
        if(horse.getOwnerUuid() == null) return null;
        return HorseAbilityHandler.getOrAddHorseAbility(horse.getOwnerUuid(), getUuid());
    }

    @Unique
    private void setHorseNameTag() {
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
        return horse.getOwnerUuid() != null;
    }

    @Unique
    private void setImmortal () {
        horse.setInvulnerable(true);
    }

    // 미친 정도에 따라 멈출지 여부
    @Unique
    private boolean isHorseCrazy() {
        HorseAbility horseAbility = getHorseAbility();
        return horseAbility != null && horseAbility.crazyFactor > Math.random();
    }


    @Unique
    private void setRandCrazyCount() {
        crazyCount = (int) (Math.random() * MAX_CRAZY_COUNT);
    }
}
