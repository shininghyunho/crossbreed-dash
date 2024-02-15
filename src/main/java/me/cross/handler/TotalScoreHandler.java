package me.cross.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TotalScoreHandler {
    // 유저마다 점수를 저장하는 맵
    private static final Map<UUID, Integer> totalScore = new HashMap<>();

    // getter
    public static int getTotalScore(UUID playerUUID) {
        if(!totalScore.containsKey(playerUUID)) setTotalScore(playerUUID, 0);
        return totalScore.get(playerUUID);
    }
    // adder
    public static void addTotalScore(UUID playerUUID, int score) {
        setTotalScore(playerUUID, getTotalScore(playerUUID) + score);
    }
    // setter
    private static void setTotalScore(UUID playerUUID, int score) {
        totalScore.put(playerUUID, score);
    }

    public static Map<UUID, Integer> getTotalScoreMap() {
        return totalScore;
    }
}
