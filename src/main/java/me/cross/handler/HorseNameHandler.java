package me.cross.handler;

import net.minecraft.nbt.NbtCompound;

import java.util.*;

public class HorseNameHandler {
    // 말 이름을 랜덤하게 만들고 Map 에 저장
    private static final Map<UUID,String> horseNameMap = new HashMap<>();
    private static final List<String> ADJECTIVES = Arrays.asList(
            "느낌있는", "세련된", "강력한", "빠른", "영리한", "아름다운", "멋진", "웅장한", "신비로운", "독특한"
    );

    private static final List<String> NOUNS = Arrays.asList(
            "주먹", "돌진", "번개", "바람", "별", "달", "태양", "불꽃", "바다", "산"
    );

    public static String getHorseName(UUID uuid) {
        String name = horseNameMap.get(uuid);

        if (name == null) {
            name = getRandomName();
            horseNameMap.put(uuid, name);
        }

        return name;
    }

    private static String getRandomName() {
        Random random = new Random();
        int adjectiveIndex = random.nextInt(ADJECTIVES.size());
        int nounIndex = random.nextInt(NOUNS.size());

        String adjective = ADJECTIVES.get(adjectiveIndex);
        String noun = NOUNS.get(nounIndex);

        return adjective + " " + noun;
    }

    // write to nbt
    public static void writeToNbt(NbtCompound nbt) {
        NbtCompound horseNameMapNbt = new NbtCompound();
        for(UUID uuid : horseNameMap.keySet()) {
            horseNameMapNbt.putString(uuid.toString(), horseNameMap.get(uuid));
        }
        nbt.put("HorseNameMap", horseNameMapNbt);
    }

    // read from nbt
    public static void readFromNbt(NbtCompound nbt) {
        NbtCompound horseNameMapNbt = nbt.getCompound("HorseNameMap");
        for(String uuidString : horseNameMapNbt.getKeys()) {
            UUID uuid = UUID.fromString(uuidString);
            String name = horseNameMapNbt.getString(uuidString);
            horseNameMap.put(uuid, name);
        }
    }
}
