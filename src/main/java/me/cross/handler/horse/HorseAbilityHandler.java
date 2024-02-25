package me.cross.handler.horse;

import me.cross.Cross;
import me.cross.entity.HorseAbility;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseAbilityHandler {
    private static final String HORSE_ABILITIES_MAP_KEY = "HorseAbilitiesMap";
    public static final Map<UUID,Map<UUID, @NotNull HorseAbility>> horseAbilitiesMap = new HashMap<>();

    public static void addHorseAbility(UUID playerUUID, UUID horseUUID, HorseAbility horseAbility) {
        if(playerUUID == null || horseUUID == null || horseAbility == null) return;

        if(horseAbilitiesMap.containsKey(playerUUID)) {
            horseAbilitiesMap.get(playerUUID).put(horseUUID, horseAbility);
        } else {
            Map<UUID,HorseAbility> horseAbilityMap = new HashMap<>();
            horseAbilityMap.put(horseUUID, horseAbility);
            horseAbilitiesMap.put(playerUUID, horseAbilityMap);
        }
    }

    public static @NotNull HorseAbility getOrAddHorseAbility(@NotNull UUID playerUUID, @NotNull UUID horseUUID) {
        if(isHorseAbilityExist(playerUUID, horseUUID)) return horseAbilitiesMap.get(playerUUID).get(horseUUID);

        Cross.LOGGER.info("HorseAbility 없으므로 새로 생성합니다.");
        HorseAbility newHorseAbility = new HorseAbility(playerUUID, horseUUID);
        addHorseAbility(playerUUID, horseUUID, newHorseAbility);
        return newHorseAbility;
    }

    public static HorseAbility getHorseAbility(UUID playerUUID, String horseName) {
        if(horseAbilitiesMap.containsKey(playerUUID)) {
            for(UUID horseUUID : horseAbilitiesMap.get(playerUUID).keySet()) {
                if(horseAbilitiesMap.get(playerUUID).get(horseUUID).name.equals(horseName)) {
                    return horseAbilitiesMap.get(playerUUID).get(horseUUID);
                }
            }
        }
        return null;
    }

    // isHorseAbilityExist
    public static boolean isHorseAbilityExist(UUID playerUUID, UUID horseUUID) {
        return horseAbilitiesMap.containsKey(playerUUID) && horseAbilitiesMap.get(playerUUID).containsKey(horseUUID);
    }

    public static void writeToNbt(NbtCompound nbt) {
        // 3차원 nbt
        NbtCompound horseAbilitiesMapNbt = new NbtCompound();
        for(UUID playerUUID : horseAbilitiesMap.keySet()) {
            // 2차원 nbt
            NbtCompound horseAbilityMapNbt = new NbtCompound();
            // 1차원 Map
            Map<UUID,HorseAbility> horseAbilityMap = horseAbilitiesMap.get(playerUUID);
            for(UUID horseUUID : horseAbilityMap.keySet()) {
                // 1차원 nbt
                NbtCompound horseAbilityNbt = new NbtCompound();
                // 1차원 nbt 에 HorseAbility 저장
                horseAbilityMap.get(horseUUID).writeToNbt(horseAbilityNbt);
                // 1차원 nbt 을 2차원 nbt 에 저장
                horseAbilityMapNbt.put(horseUUID.toString(), horseAbilityNbt);
            }
            // 2차원 nbt 을 3차원 nbt 에 저장
            horseAbilitiesMapNbt.put(playerUUID.toString(), horseAbilityMapNbt);
        }
        // 3차원 nbt 을 전체 nbt 에 저장
        nbt.put(HORSE_ABILITIES_MAP_KEY, horseAbilitiesMapNbt);
    }

    public static void readFromNbt(NbtCompound nbt) {
        NbtCompound horseAbilitiesMapNbt = nbt.getCompound(HORSE_ABILITIES_MAP_KEY);

        for(String playerUUIDString : horseAbilitiesMapNbt.getKeys()) {
            UUID playerUUID = UUID.fromString(playerUUIDString);
            NbtCompound horseAbilityMapNbt = horseAbilitiesMapNbt.getCompound(playerUUIDString);

            for(String horseUUIDString : horseAbilityMapNbt.getKeys()) {
                UUID horseUUID = UUID.fromString(horseUUIDString);
                NbtCompound horseAbilityNbt = horseAbilityMapNbt.getCompound(horseUUIDString);

                HorseAbility horseAbility = new HorseAbility(playerUUID, horseUUID);
                horseAbility.readFromNbt(horseAbilityNbt);

                HorseAbilityHandler.addHorseAbility(playerUUID, horseUUID, horseAbility);
            }
        }
    }
}
