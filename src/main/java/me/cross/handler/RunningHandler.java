package me.cross.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RunningHandler {
    // 유저마다 체크포인트 지났는지 여부 체크
    // UUID 하나당 체크 포인트 지난 여부 체크
    // 체크포인트는 순서대로 지나야함
    // 체크포인트는 총 10개
    private static final int CHECKPOINT_COUNT = 10;
    private static final Map<UUID,boolean[]> playerCheckpoints = new HashMap<>();

    public static void addPlayers(List<UUID> players) {
        playerCheckpoints.clear();
        players.forEach(player -> playerCheckpoints.put(player, new boolean[CHECKPOINT_COUNT]));
    }

    // 체크포인트를 순서대로 밟았는지 확인
    public static boolean isPassedInorder(UUID player, int checkpointIdx) {
        if(!playerCheckpoints.containsKey(player)) return false;
        else if(checkpointIdx==0) return true;

        return playerCheckpoints.get(player)[checkpointIdx-1];
    }
}
