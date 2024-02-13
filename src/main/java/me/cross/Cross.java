package me.cross;

import me.cross.custom.CustomBlock;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.custom.event.race.RacingCallback;
import me.cross.entity.HorseAbility;
import me.cross.handler.HorseOwnerHandler;
import me.cross.handler.RacingHandler;
import me.cross.handler.RacingTimer;
import me.cross.handler.Stopwatch;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cross implements ModInitializer {
	public static final String MOD_ID = "cross";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final long RACING_INTERVAL_SEC = 60 * 5, RUNNING_READY_SEC = 60, COUNTDOWN_SEC = 5, FINISHED_SEC = 0;
	private static final Stopwatch stopwatchForRacingReady = new Stopwatch(RACING_INTERVAL_SEC, Stopwatch.MODE.READY_FOR_RACING);
	private static final Stopwatch stopwatchForRunningReady = new Stopwatch(RUNNING_READY_SEC, Stopwatch.MODE.READY_FOR_RUNNING);
	private static final Stopwatch stopwatchForCountdown = new Stopwatch(COUNTDOWN_SEC, Stopwatch.MODE.COUNTDOWN);
	private static final Stopwatch stopwatchForFinished = new Stopwatch(FINISHED_SEC, Stopwatch.MODE.FINISHED);
	private static RacingHandler.MODE racingMode = RacingHandler.MODE.NOT_STARTED;

	@Override
	public void onInitialize() {
		registerEvents();
		CustomBlock.registerCustomBlock();
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
			LOGGER.info("stopwatch start");
			if(RacingHandler.mode == RacingHandler.MODE.NOT_STARTED) stopwatchForRacingReady.start();
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			LOGGER.info("stopwatch stop");
			if(RacingHandler.mode == RacingHandler.MODE.NOT_STARTED) stopwatchForRacingReady.pause();
		});
		// for every tick
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			// 중복 호출 방지
			if(racingMode == RacingHandler.mode) return;
			racingMode = RacingHandler.mode;

			broadcastRacingEvent(server);
		});
	}

	private void registerRacingEvents() {
		// 레이싱 준비.
		RacingCallback.READY_FOR_RACING.register(() -> {
			LOGGER.info("레이싱 준비 이벤트");
			stopwatchForRacingReady.stop();
			stopwatchForRunningReady.start();
			RacingHandler.readyForRacing();
			return ActionResult.PASS;
		});

		// 달리기 준비.
		RacingCallback.READY_FOR_RUNNING.register(() -> {
			LOGGER.info("달리기 준비 이벤트");
			stopwatchForRunningReady.stop();
			stopwatchForCountdown.start();
			RacingHandler.readyForRunning();
			return ActionResult.PASS;
		});

		// Running TODO : 아직 호출하는 곳이 없음
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

		// END TODO : 아직 호출하는 곳이 없음
		RacingCallback.END.register(() -> {
			LOGGER.info("경주 완전 종료 이벤트, 다음 경기 준비");
			stopwatchForFinished.stop();
			stopwatchForRacingReady.start();
			RacingHandler.end();
			return ActionResult.PASS;
		});
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
		if(RacingHandler.mode == RacingHandler.MODE.READY_FOR_RACING) {
			broadcast("경주가 곧 시작됩니다. 준비준비~", server);
			// register all players
			RacingHandler.addPlayers(server.getPlayerManager().getPlayerList());
		}
		else if(RacingHandler.mode == RacingHandler.MODE.READY_FOR_RUNNING) {
			broadcast("출발선에 서주세요. 곧 출발합니다!", server);
		}
		else if(RacingHandler.mode == RacingHandler.MODE.RUNNING) {
			broadcast("경주 시작! 달리세요!!", server);
		}
		else if(RacingHandler.mode == RacingHandler.MODE.FINISHED) {
			broadcast("모두 도착했습니다. 경주가 종료되었습니다.", server);
		}
		else if(RacingHandler.mode == RacingHandler.MODE.NOT_STARTED) {
			broadcast("이번 경기가 완전히 종료되었습니다.", server);
		}
	}
	private void broadcast(String message, MinecraftServer server) {
		PlayerManager playerManager = server.getPlayerManager();
		Text text = Text.of(message);
		playerManager.broadcast(text, false);
	}
}