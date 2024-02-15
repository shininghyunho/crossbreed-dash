package me.cross.handler;

import me.cross.Cross;
import me.cross.entity.HorseAbility;
import net.minecraft.util.math.BlockPos;

public class HorseSummonHandler {
    private static boolean isHorseSummoned = false;
    private static HorseAbility horseAbility;
    private static BlockPos pos;

    public static boolean isHorseSummoned() {
        return isHorseSummoned;
    }

    // when user execute /horse command
    public static void summonHorse(String horseName1, String horseName2) {
        // HorseBreederHandler 에게서 소환할 말의 ability, pos 를 전달받아서 소환
        horseAbility = BreedHandler.breedHorse(horseName1, horseName2);
        pos = BreedHandler.getSummonPos();

        if(horseAbility != null && pos != null){
            isHorseSummoned = true;
            Cross.LOGGER.info("말을 소환합니다. 이히힝~!");
        }
    }

    // summon horse finished
    public static void summonHorseFinished() {
        isHorseSummoned = false;
    }

    public static HorseAbility getHorseAbility() {
        return horseAbility;
    }

    public static BlockPos getSummonPos() {
        return pos;
    }
}
