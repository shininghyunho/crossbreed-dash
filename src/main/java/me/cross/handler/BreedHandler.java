package me.cross.handler;

import me.cross.Cross;
import me.cross.entity.HorseAbility;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class BreedHandler {
    private static final Random random = new Random();
    public static HorseAbility breedHorse(String horseName1, String horseName2, UUID playerUUID, BlockPos pos) {
        HorseAbility horseAbility1 = HorseAbilityHandler.getHorseAbility(playerUUID, horseName1);
        HorseAbility horseAbility2 = HorseAbilityHandler.getHorseAbility(playerUUID, horseName2);

        if (isImpossibleBreed(horseAbility1, horseAbility2, playerUUID)) return null;
        // 1,2 를 랜덤하게 섞어서 새로운 HorseAbility 생성
        return breedHorseAbility(horseAbility1, horseAbility2, playerUUID, pos);
    }

    private static boolean isImpossibleBreed(HorseAbility horseAbility1, HorseAbility horseAbility2, UUID playerUUID) {
        if(horseAbility1 == null) {
            Cross.broadcast("1번째 말은 당신 소유가 아닙니다");
            return true;
        }
        else if(horseAbility2 == null) {
            Cross.broadcast("2번째 말은 당신 소유가 아닙니다");
            return true;
        }
        else if(!horseAbility1.ownerUuid.equals(playerUUID)) {
            Cross.broadcast(horseAbility1.name + " 은 당신의 말이 아닙니다.");
            return true;
        }
        else if(!horseAbility2.ownerUuid.equals(playerUUID)) {
            Cross.broadcast(horseAbility2.name + " 은 당신의 말이 아닙니다.");
            return true;
        }
        else if(horseAbility1.horseUuid.equals(horseAbility2.horseUuid)) {
            Cross.broadcast("같은 말입니다.");
            return true;
        }
        else if(horseAbility1.isBred) {
            Cross.broadcast(horseAbility1.name + " 은 이미 번식한 말입니다.");
            return true;
        }
        else if(horseAbility2.isBred) {
            Cross.broadcast(horseAbility2.name + " 은 이미 번식한 말입니다.");
            return true;
        }
        return false;
    }

    private static HorseAbility breedHorseAbility(HorseAbility horseAbility1, HorseAbility horseAbility2, UUID playerUUID, BlockPos pos) {
        // 1,2 를 랜덤하게 섞어서 새로운 HorseAbility 생성
        AbstractHorseEntity horseEntity = HorseSummonHandler.summonHorse(pos);
        HorseAbility newHorseAbility = new HorseAbility(playerUUID, Objects.requireNonNull(horseEntity).getUuid());

        // 번식 여부를 true 로 변경
        horseAbility1.isBred = true;
        horseAbility2.isBred = true;

        // 능력을 섞음
        newHorseAbility.speedMultiplier = mixSpeedMultipliers(horseAbility1, horseAbility2);
        newHorseAbility.jumpMultiplier = mixJumpMultipliers(horseAbility1, horseAbility2);
        newHorseAbility.crazyFactor = mixCrazyFactors(horseAbility1, horseAbility2);
        return newHorseAbility;
    }

    private static float mixSpeedMultipliers(HorseAbility horseAbility1, HorseAbility horseAbility2) {
        float bigger = Math.max((float)horseAbility1.speedMultiplier, (float)horseAbility2.speedMultiplier);
        float smaller = Math.min((float)horseAbility1.speedMultiplier, (float)horseAbility2.speedMultiplier);
        float min = bigger - smaller;
        float max = bigger + smaller;
        return getRandomFloatInRange(min, max);
    }

    private static float mixJumpMultipliers(HorseAbility horseAbility1, HorseAbility horseAbility2) {
        float bigger = Math.max((float)horseAbility1.jumpMultiplier, (float)horseAbility2.jumpMultiplier);
        float min = 0.5F;
        float max = bigger * 2;
        return getRandomFloatInRange(min, max);
    }

    private static float mixCrazyFactors(HorseAbility horseAbility1, HorseAbility horseAbility2) {
        float bigger = Math.max((float)horseAbility1.crazyFactor, (float)horseAbility2.crazyFactor);
        float smaller = Math.min((float)horseAbility1.crazyFactor, (float)horseAbility2.crazyFactor);
        float min = smaller * 1.5F;
        float max = bigger * 1.5F;
        return getRandomFloatInRange(min, max);
    }

    private static float getRandomFloatInRange(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

}
