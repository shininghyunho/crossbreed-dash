package me.cross.handler;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class RacingHandler {
    public static RacingMode mode = RacingMode.NOT_STARTED;

    private static final Set<UUID> playersUUID = new HashSet<>();

    public static void readyForRunning() {
        // add players to RunningHandler
        playersUUID.clear();
        RunningHandler.addPlayers(new ArrayList<>(playersUUID));
        mode = RacingMode.READY_FOR_RUNNING;
        // 플레이어가 체크포인트 0에 도착했다면 움직일 수 없게 설정 (mixin 에서 처리)
    }
    public static void countdown() {
        mode = RacingMode.COUNTDOWN;
    }

    public static void run() {
        mode = RacingMode.RUNNING;
    }

    public static void finished() {
        mode = RacingMode.FINISHED;
    }

    public static void end() {
        mode = RacingMode.NOT_STARTED;
    }

    public static void addPlayers(List<ServerPlayerEntity> players) {
        playersUUID.clear();
        players.forEach(player -> playersUUID.add(player.getUuid()));
    }

    // is ready?
    public static boolean isCountdown() {
        return mode == RacingMode.COUNTDOWN;
    }

    public static boolean isRunning() {
        return mode == RacingMode.RUNNING;
    }

    public static void init() {
        mode = RacingMode.NOT_STARTED;
    }
}
