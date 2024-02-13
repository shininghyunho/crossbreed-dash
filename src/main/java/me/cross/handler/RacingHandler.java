package me.cross.handler;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class RacingHandler {
    public enum MODE {
        // 경기가 시작되지 않음
        NOT_STARTED,
        // 경기 이벤트가 시작
        READY_FOR_RACING,
        // 출발선에서 경주 준비
        READY_FOR_RUNNING,
        // 경주 중
        RUNNING,
        // 모두 도착
        FINISHED
    }
    public static MODE mode = MODE.NOT_STARTED;

    private static final Set<UUID> playersUUID = new HashSet<>();

    public static void readyForRacing() {
        mode = MODE.READY_FOR_RACING;
    }

    public static void readyForRunning() {
        // add players to RunningHandler
        playersUUID.clear();
        RunningHandler.addPlayers(new ArrayList<>(playersUUID));
        mode = MODE.READY_FOR_RUNNING;
        // 플레이어가 체크포인트 0에 도착했다면 움직일 수 없게 설정 (mixin 에서 처리)
    }

    public static void run() {
        mode = MODE.RUNNING;
    }

    public static void finished() {
        mode = MODE.FINISHED;
    }

    public static void end() {
        mode = MODE.NOT_STARTED;
    }

    public static void addPlayers(List<ServerPlayerEntity> players) {
        playersUUID.clear();
        players.forEach(player -> playersUUID.add(player.getUuid()));
    }

    // 움직이지 못하는 모드입니까?
    public static boolean isNotMoveableMode() {
        return mode == MODE.READY_FOR_RACING || mode == MODE.READY_FOR_RUNNING;
    }
}
