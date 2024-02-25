package me.cross.handler.horse;

import me.cross.Cross;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class HorseSummonHandler {
    private static final Random random = new Random();

    public static void summonBreedHorse(String horseName1, String horseName2, UUID playerUUID) {
        // HorseBreederHandler 에게서 소환할 말의 ability, pos 를 전달받아서 소환
        BlockPos pos = Objects.requireNonNull(Cross.server.getPlayerManager().getPlayer(playerUUID)).getBlockPos();
        BreedHandler.breedHorse(horseName1, horseName2, playerUUID, pos);
    }

    public static AbstractHorseEntity summonHorse(BlockPos pos) {
        // 소환 실패하면 null 반환
        HorseEntity horse = EntityType.HORSE.create(Cross.server.getWorld(World.OVERWORLD));

        // 랜덤 색상
        if(horse != null) {
            horse.setVariant(HorseColor.byId(random.nextInt(7)));
        }

        if(horse == null) return null;
        horse.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        try {
            Objects.requireNonNull(Cross.server.getWorld(World.OVERWORLD)).spawnEntityAndPassengers(horse);
        } catch (Exception e) {
            Cross.LOGGER.error("말 소환 실패");
            return null;
        }
        Cross.LOGGER.info("말 소환 성공");
        return horse;
    }
}
