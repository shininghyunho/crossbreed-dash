package me.cross.handler;

import me.cross.Cross;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class RacingHandler {
    public static RacingMode mode = RacingMode.NOT_STARTED;

    private static final Set<UUID> playersUUID = new HashSet<>();

    public static void readyForRunning() {
        // add players to RunningHandler
        Cross.broadcast("경주가 곧 시작됩니다. 출발선에 서주세요.");
        mode = RacingMode.READY_FOR_RUNNING;

        StopwatchHandler.forNotStarted.stop();
        StopwatchHandler.forRunningReady.start();

        addPlayers(Cross.server.getPlayerManager().getPlayerList());
        RunningHandler.addPlayers(new ArrayList<>(playersUUID));
    }
    public static void countdown() {
        Cross.broadcast("카운트다운 시작!");
        mode = RacingMode.COUNTDOWN;

        StopwatchHandler.forRunningReady.stop();
        StopwatchHandler.forCountdown.start();
    }

    public static void run() {
        Cross.broadcast("경주 시작! 달리세요!!");
        mode = RacingMode.RUNNING;

        StopwatchHandler.forCountdown.stop();

        RacingTimer.start();
        RacingHandler.run();
    }

    public static void finished() {
        Cross.broadcast("모두 도착했습니다. 경주가 종료되었습니다. 순위를 확인해보세요.");
        mode = RacingMode.FINISHED;

        RacingTimer.stop();

        StopwatchHandler.forFinished.start();
    }

    public static void end() {
        Cross.broadcast("경주가 종료되었습니다. 다음 경주를 준비하세요.");
        mode = RacingMode.NOT_STARTED;

        StopwatchHandler.forFinished.stop();
        StopwatchHandler.forNotStarted.start();
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
