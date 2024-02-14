package me.cross;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.cross.custom.CustomBlock;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.custom.event.race.RacingCallback;
import me.cross.entity.HorseAbility;
import me.cross.handler.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class Cross implements ModInitializer {
	public static final String MOD_ID = "cross";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static MinecraftServer crossServer;
	private static final long RACING_INTERVAL_SEC = 20, RUNNING_READY_SEC = 10, COUNTDOWN_SEC = 10, FINISHED_SEC = 60;
	private static final Stopwatch stopwatchForNotStarted = new Stopwatch(RACING_INTERVAL_SEC, RacingMode.NOT_STARTED);
	private static final Stopwatch stopwatchForRunningReady = new Stopwatch(RUNNING_READY_SEC, RacingMode.READY_FOR_RUNNING);
	private static final Stopwatch stopwatchForCountdown = new Stopwatch(COUNTDOWN_SEC, RacingMode.COUNTDOWN);
	private static final Stopwatch stopwatchForFinished = new Stopwatch(FINISHED_SEC, RacingMode.FINISHED);
	private static RacingMode racingMode = RacingMode.NOT_STARTED;

	@Override
	public void onInitialize() {
		registerEvents();
		CustomBlock.registerCustomBlock();
		registerModCommands();
		CheckPointBlockHandler.initCheckPointBlockPosMap();
		LOGGER.info("Hello Fabric world!");
	}

	private void registerEvents() {
		registerHorseEvents();
		registerServerEvents();
		registerRacingEvents();
	}

	private void registerHorseEvents() {
		// HorseBondWithPlayerCallback.EVENT.register
		HorseBondWithPlayerCallback.EVENT.register((player, horse) -> {
			LOGGER.info("말과 상호작용 이벤트 발생");
			addAbility(player, horse);
			return ActionResult.PASS;
		});
	}

	private void registerServerEvents() {
		// server start, stop
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LOGGER.info("server start");
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			LOGGER.info("server stop");
		});
		// for every tick
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if(crossServer == null) crossServer = server;
			// 중복 호출 방지
			if(racingMode == RacingHandler.mode) return;
			racingMode = RacingHandler.mode;

			broadcastRacingEvent(server);
		});
	}

	private static void stopAllStopwatches() {
		stopwatchForNotStarted.stop();
		stopwatchForRunningReady.stop();
		stopwatchForCountdown.stop();
		stopwatchForFinished.stop();
	}
	private void registerRacingEvents() {
		// 달리기 준비.
		RacingCallback.READY_FOR_RUNNING.register(() -> {
			LOGGER.info("달리기 준비 이벤트");
			stopwatchForNotStarted.stop();
			stopwatchForRunningReady.start();
			RacingHandler.readyForRunning();
			return ActionResult.PASS;
		});

		// 카운트다운
		RacingCallback.COUNTDOWN.register(() -> {
			LOGGER.info("카운트다운 이벤트");
			stopwatchForRunningReady.stop();
			stopwatchForCountdown.start();
			RacingHandler.countdown();
			return ActionResult.PASS;
		});

		// Running
		RacingCallback.RUNNING.register(() -> {
			LOGGER.info("달리기 이벤트");
			stopwatchForCountdown.stop();
			RacingTimer.start();
			RacingHandler.run();
			return ActionResult.PASS;
		});

		// Finished TODO : 아직 호출하는 곳이 없음
		RacingCallback.FINISHED.register(() -> {
			LOGGER.info("경주 종료 이벤트");
			RacingTimer.stop();
			stopwatchForFinished.start();
			RacingHandler.finished();
			return ActionResult.PASS;
		});

		// END
		RacingCallback.END.register(() -> {
			LOGGER.info("경주 완전 종료 이벤트, 다음 경기 준비");
			stopwatchForFinished.stop();
			stopwatchForNotStarted.start();
			RacingHandler.end();
			return ActionResult.PASS;
		});
	}
	private void registerModCommands() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			registerFromDispatcher(dispatcher);
		}));
	}
	private void registerFromDispatcher(CommandDispatcher<ServerCommandSource> dispatcher) {
		// execute startMod
		dispatcher.register(literal("startMod").executes(context -> {
			startMod();
			return Command.SINGLE_SUCCESS;
		}));

		// execute stopMod
		dispatcher.register(literal("stopMod").executes(context -> {
			stopMod();
			return Command.SINGLE_SUCCESS;
		}));
	}
	private static void startMod() {
		LOGGER.info("startMod");
		broadcast("경주가 시작됩니다. 출발선에 서주세요.");
		stopwatchForNotStarted.start();
		RacingHandler.init();
	}
	private static void stopMod() {
		LOGGER.info("stopMod");
		broadcast("경주가 완전히 종료되었습니다. 다음 경주를 기다려주세요.");
		stopAllStopwatches();
		RacingHandler.init();
	}
	private void addAbility(PlayerEntity player, AbstractHorseEntity horse) {
		// map 에서 houseAbility 가 있으면 가져오고 없으면 생성
		if(HorseOwnerHandler.containsHorseAbility(player.getUuid(), horse.getUuid())) {
			Cross.LOGGER.info("horseAbility is already exist");
			HorseAbility horseAbility = HorseOwnerHandler.getHorseAbility(player.getUuid(), horse.getUuid());

			if(horseAbility!=null) Cross.LOGGER.info("horseAbility : " + horseAbility);
		} else {
			Cross.LOGGER.info("horseAbility is not exist so create new one");
			HorseAbility horseAbility = new HorseAbility(player.getUuid(), horse.getUuid());
			HorseOwnerHandler.addHorseAbility(player.getUuid(), horse.getUuid(), horseAbility);

			Cross.LOGGER.info("horseAbility : " + horseAbility);
		}
	}
	private void broadcastRacingEvent(MinecraftServer server) {
		if(RacingHandler.mode == RacingMode.READY_FOR_RUNNING) {
			broadcast("경주가 곧 시작됩니다. 출발선에 서주세요.");
			// register all players
			RacingHandler.addPlayers(server.getPlayerManager().getPlayerList());
		}
		else if(RacingHandler.mode == RacingMode.COUNTDOWN) {
			broadcast("카운트다운 시작!");
		}
		else if(RacingHandler.mode == RacingMode.RUNNING) {
			broadcast("경주 시작! 달리세요!!");
		}
		else if(RacingHandler.mode == RacingMode.FINISHED) {
			broadcast("모두 도착했습니다. 경주가 종료되었습니다. 순위를 확인해보세요.");
		}
		else if(RacingHandler.mode == RacingMode.NOT_STARTED) {
			broadcast("이번 경기가 완전히 종료되었습니다. 다음 경기를 기다려주세요.");
		}
	}
	private static void broadcast(String message) {
		if(crossServer == null) return;
		PlayerManager playerManager = crossServer.getPlayerManager();
		Text text = Text.of(message);
		playerManager.broadcast(text, false);
	}
}