package me.cross.handler.race;

import me.cross.Cross;
import me.cross.handler.MessageHandler;
import me.cross.handler.stopwatch.StopwatchHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class RacingHandler {
    public static RacingMode mode = RacingMode.NOT_STARTED;

    private static final Set<UUID> playersUUID = new HashSet<>();

    public static void readyForRunning() {
        // add players to RunningHandler
        MessageHandler.broadcast("경주가 곧 시작됩니다. 출발선에 서주세요.");
        mode = RacingMode.READY_FOR_RUNNING;

        StopwatchHandler.forNotStarted.stop();
        StopwatchHandler.forRunningReady.start();

        addPlayers(Cross.server.getPlayerManager().getPlayerList());
    }
    public static void countdown() {
        MessageHandler.broadcast("카운트다운 시작!");
        mode = RacingMode.COUNTDOWN;

        StopwatchHandler.forRunningReady.stop();
        StopwatchHandler.forCountdown.start();
    }

    public static void run() {
        MessageHandler.broadcast("경주 시작! 달리세요!!");
        mode = RacingMode.RUNNING;

        StopwatchHandler.forCountdown.stop();
        RacingTimer.start();

        RunningHandler.addPlayers(new ArrayList<>(playersUUID));

    }

    public static void finished() {
        MessageHandler.broadcast("모두 도착했습니다. 경주가 종료되었습니다. 순위를 확인해보세요.");
        mode = RacingMode.FINISHED;

        RacingTimer.stop();
        StopwatchHandler.forFinished.start();
    }

    public static void end() {
        MessageHandler.broadcast("경주가 종료되었습니다. 다음 경주를 준비하세요.");
        mode = RacingMode.NOT_STARTED;

        StopwatchHandler.forFinished.stop();
        StopwatchHandler.forNotStarted.start();

        MessageHandler.broadcast(RunningHandler.getTotalResult());

        // 전체 점수 scoreboard 에 반영
        ScoreboardHandler.updateScoreboard();

        // 전체 참가자들에게 말 알을 인벤토리에 추가
        addHorseEggItemToAllPlayers();
    }

    public static void addPlayers(List<ServerPlayerEntity> players) {
        playersUUID.clear();
        players.forEach(player -> playersUUID.add(player.getUuid()));
    }
    public static boolean isReadyForRunning() {
        return mode == RacingMode.READY_FOR_RUNNING;
    }
    public static boolean isCountdown() {
        return mode == RacingMode.COUNTDOWN;
    }
    public static boolean isRunning() {
        return mode == RacingMode.RUNNING;
    }
    public static void init() {
        mode = RacingMode.NOT_STARTED;
    }
    private static void addHorseEggItemToAllPlayers() {
        // 모든 플레이어에게 말 알을 인벤토리에 추가
        Cross.server.getPlayerManager().getPlayerList().forEach(player -> {
            Item item = Items.HORSE_SPAWN_EGG;
            // player 인벤토리에 추가
            player.giveItemStack(new ItemStack(item));
        });
    }
}
