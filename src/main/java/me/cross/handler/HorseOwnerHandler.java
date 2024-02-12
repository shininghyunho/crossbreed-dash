package me.cross.handler;

import me.cross.entity.HorseAbility;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseOwnerHandler {
    private static final String HORSE_ABILITIES_MAP_KEY = "HorseAbilitiesMap";
    public static final Map<UUID,Map<UUID, HorseAbility>> horseAbilitiesMap = new HashMap<>();

    public static void addHorseAbility(UUID playerUUID, UUID horseUUID, HorseAbility horseAbility) {
        if(horseAbilitiesMap.containsKey(playerUUID)) {
            horseAbilitiesMap.get(playerUUID).put(horseUUID, horseAbility);
        } else {
            Map<UUID,HorseAbility> horseAbilityMap = new HashMap<>();
            horseAbilityMap.put(horseUUID, horseAbility);
            horseAbilitiesMap.put(playerUUID, horseAbilityMap);
        }
    }

    public static HorseAbility getHorseAbility(UUID playerUUID, UUID horseUUID) {
        if(horseAbilitiesMap.containsKey(playerUUID)) {
            return horseAbilitiesMap.get(playerUUID).get(horseUUID);
        }
        return null;
    }

    public static boolean containsHorseAbility(UUID playerUUID, UUID horseUUID) {
        if(horseAbilitiesMap.containsKey(playerUUID)) {
            return horseAbilitiesMap.get(playerUUID).containsKey(horseUUID);
        }
        return false;
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

                HorseOwnerHandler.addHorseAbility(playerUUID, horseUUID, horseAbility);
            }
        }
    }
}
