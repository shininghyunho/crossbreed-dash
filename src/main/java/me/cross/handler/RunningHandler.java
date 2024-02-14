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
    private static final int CHECKPOINT_COUNT = 10, LAP_COUNT = 1;
    private static int finishedPlayerCount = 0, rank = 1;
    private static final Map<UUID,boolean[]> playerCheckpointPassed = new HashMap<>();
    // 유저마다 체크포인트 idx
    private static final Map<UUID,Integer> playerCheckpointIdx = new HashMap<>();
    // 유저마다 바퀴 수
    private static final Map<UUID,Integer> playerLapCount = new HashMap<>();
    // 유저마다 순위와 시간
    private static final Map<UUID,Long> playerTime = new HashMap<>();
    private static final Map<UUID,Integer> playerRank = new HashMap<>();

    public static void addPlayers(List<UUID> players) {
        initCheckpoints(players);
        initPlayerCheckpointIdx(players);
        initPlayerLapCount(players);
        initPlayerTime(players);
        initPlayerRank(players);

        // init variables
        finishedPlayerCount = 0;
        rank = 1;
    }
    // 체크포인트를 순서대로 밟았는지 확인

    public static boolean isPassedInorder(UUID player, int idx) {
        if(!playerCheckpointPassed.containsKey(player)) return false;
        else if(idx ==0) return true;

        return playerCheckpointPassed.get(player)[idx-1];
    }
    public static int getCheckpointIdx(UUID player) {
        if(!playerCheckpointIdx.containsKey(player)) return -1;
        return playerCheckpointIdx.get(player);
    }
    public static void setNextCheckpointIdx(UUID uuid) {
        if(!playerCheckpointIdx.containsKey(uuid)) return;
        // 이미 통과했다면 리턴
        int idx = playerCheckpointIdx.get(uuid);
        if(idx <0 || idx >= CHECKPOINT_COUNT) return;
        if(playerCheckpointPassed.get(uuid)[idx]) return;
        playerCheckpointIdx.put(uuid, idx+1);
    }
    public static void setPassed(UUID uuid, int idx) {
        if(!playerCheckpointPassed.containsKey(uuid)) return;
        playerCheckpointPassed.get(uuid)[idx] = true;
    }
    // 1 바퀴 돌았는지 확인
    public static boolean isLapFinished(UUID uuid) {
        if(!playerCheckpointIdx.containsKey(uuid)) return false;
        return playerCheckpointIdx.get(uuid) >= CHECKPOINT_COUNT;
    }
    public static void setNextLap(UUID uuid) {
        if(!playerLapCount.containsKey(uuid)) return;

        // lap 을 증가하고 끝났는지 확인
        addPlayerLapCount(uuid);
        if(isPlayerFinished(uuid)) {
            //  끝났다면 랭킹과 시간 저장
            playerTime.put(uuid, RacingTimer.getTime());
            playerRank.put(uuid, rank++);
            finishedPlayerCount++;

            // 모든 플레이어가 끝났다면 경주 종료
            checkAllPlayerFinished();
        }
        // 체크포인트 초기화
        clearCheckpoint(uuid);
    }

    public static String getResult() {
        return "경주 결과";
    }

    private static void checkAllPlayerFinished() {
        if(finishedPlayerCount >= playerLapCount.size()) {
            RacingHandler.finished();
            // TODO : 러닝 결과 안내

        }
    }

    private static void addPlayerLapCount(UUID uuid) {
        if(!playerLapCount.containsKey(uuid)) return;
        playerLapCount.put(uuid, playerLapCount.get(uuid)+1);
    }
    private static boolean isPlayerFinished(UUID uuid) {
        // 해당 유저가 끝났는지 확인
        if(!playerLapCount.containsKey(uuid)) return false;
        return playerLapCount.get(uuid) >= LAP_COUNT;
    }
    private static void clearCheckpoint(UUID uuid) {
        playerCheckpointIdx.put(uuid, 0);
        for(int i=0; i<CHECKPOINT_COUNT; i++) playerCheckpointPassed.get(uuid)[i] = false;
    }

    private static void initCheckpoints(List<UUID> players) {
        playerCheckpointPassed.clear();
        players.forEach(player -> playerCheckpointPassed.put(player, new boolean[CHECKPOINT_COUNT]));
    }
    private static void initPlayerCheckpointIdx(List<UUID> players) {
        playerCheckpointIdx.clear();
        players.forEach(player -> playerCheckpointIdx.put(player, 0));
    }
    private static void initPlayerLapCount(List<UUID> players) {
        playerLapCount.clear();
        players.forEach(player -> playerLapCount.put(player, 0));
    }
    private static void initPlayerTime(List<UUID> players) {
        playerTime.clear();
        players.forEach(player -> playerTime.put(player, 0L));
    }
    private static void initPlayerRank(List<UUID> players) {
        playerRank.clear();
        for(int i=0; i<players.size(); i++) playerRank.put(players.get(i), i+1);
    }
}
