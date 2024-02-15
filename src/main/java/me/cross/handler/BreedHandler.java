package me.cross.handler;

import me.cross.Cross;
import me.cross.entity.HorseAbility;
import net.minecraft.util.math.BlockPos;

public class BreedHandler {
    public static HorseAbility breedHorse(String horseName1, String horseName2) {
        HorseAbility horseAbility1 = HorseAbilityHandler.getHorseAbility(horseName1);
        HorseAbility horseAbility2 = HorseAbilityHandler.getHorseAbility(horseName2);

        if (isImpossibleBreed(horseAbility1, horseAbility2)) return null;
        // 1,2 를 랜덤하게 섞어서 새로운 HorseAbility 생성
        return breedHorseAbility(horseAbility1, horseAbility2);
    }

    private static boolean isImpossibleBreed(HorseAbility horseAbility1, HorseAbility horseAbility2) {
        if(horseAbility1 == null || horseAbility2 == null) {
            Cross.broadcast("해당 이름의 말이 존재하지 않습니다.");
            return true;
        }
        else if(!horseAbility1.ownerUuid.equals(horseAbility2.ownerUuid)) {
            Cross.broadcast("각각의 말을 소유한 플레이어가 다릅니다.");
            return true;
        }
        else if(horseAbility1.horseUuid.equals(horseAbility2.horseUuid)) {
            Cross.broadcast("같은 말입니다.");
            return true;
        }
        return false;
    }

    public static BlockPos getSummonPos() {
        // TODO
        return null;
    }

    private static HorseAbility breedHorseAbility(HorseAbility horseAbility1, HorseAbility horseAbility2) {
        // TODO
        return null;
    }
}
