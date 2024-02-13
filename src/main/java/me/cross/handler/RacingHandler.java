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
        // TODO : 경주 준비 로직
    }

    public static void readyForRunning() {
        mode = MODE.READY_FOR_RUNNING;
        // TODO : 경주 시작 준비 로직
        // add players to RunningHandler
        RunningHandler.addPlayers(new ArrayList<>(playersUUID));

    }

    public static void run() {
        mode = MODE.RUNNING;
        // TODO : 경주 시작 로직
    }

    public static void finished() {
        mode = MODE.FINISHED;
        // TODO : 경주 종료 로직
    }

    public static void end() {
        mode = MODE.NOT_STARTED;
        // TODO : 경주 완전 종료 로직
    }

    public static void addPlayers(List<ServerPlayerEntity> players) {
        playersUUID.clear();
        players.forEach(player -> playersUUID.add(player.getUuid()));
    }
}
