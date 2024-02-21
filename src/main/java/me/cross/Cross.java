package me.cross;

import me.cross.custom.CustomBlock;
import me.cross.custom.command.HorseBreedCommand;
import me.cross.custom.command.ModCommand;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.custom.event.race.RacingCallback;
import me.cross.custom.event.race.RacingCountdownTickCallback;
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
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cross implements ModInitializer {
	public static final String MOD_ID = "cross";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer server;

	@Override
	public void onInitialize() {
		registerEvents();
		CustomBlock.registerCustomBlock();
		registerCustomCommands();
		CheckPointBlockHandler.initCheckPointBlockPosMap();
		LOGGER.info("Hello Fabric world!");
	}

	public static void startMod() {
		LOGGER.info("start mod");
		MessageHandler.broadcastWithRed("모드가 시작되었습니다.",true);
		setModStart();
	}
	public static void stopMod() {
		LOGGER.info("stop mod");
		MessageHandler.broadcastWithRed("모드가 종료되었습니다.",true);
		setModStop();
	}

	//  Private Methods
	private void registerEvents() {
		registerHorseEvents();
		registerServerEvents();
		registerRacingEvents();
	}
	private void registerHorseEvents() {
		// HorseBondWithPlayerCallback.EVENT.register
		HorseBondWithPlayerCallback.EVENT.register((player, horse) -> {
			LOGGER.info("말과 상호작용 이벤트 발생");
			// 말 능력치가 없다면 생성
			addAbilityIfNotPresent(player, horse);
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
			if(Cross.server == null) Cross.server = server;
		});
	}
	private void registerRacingEvents() {
		// 달리기 준비.
		RacingCallback.READY_FOR_RUNNING.register(() -> {
			LOGGER.info("달리기 준비 이벤트");
			RacingHandler.readyForRunning();
			return ActionResult.PASS;
		});

		// 카운트다운
		RacingCallback.COUNTDOWN.register(() -> {
			LOGGER.info("카운트다운 이벤트");
			RacingHandler.countdown();
			return ActionResult.PASS;
		});

		// Running
		RacingCallback.RUNNING.register(() -> {
			LOGGER.info("달리기 이벤트");
			RacingHandler.run();
			return ActionResult.PASS;
		});

		// Finished
		RacingCallback.FINISHED.register(() -> {
			LOGGER.info("경주 종료 이벤트");
			RacingHandler.finished();
			return ActionResult.PASS;
		});

		// END
		RacingCallback.END.register(() -> {
			LOGGER.info("경주 완전 종료 이벤트, 다음 경기 준비");
			RacingHandler.end();
			return ActionResult.PASS;
		});

		// 카운트다운 틱
		RacingCountdownTickCallback.COUNTDOWN_TICK.register((nowTime) -> {
			MessageHandler.broadcast("카운트다운 : " + nowTime + "초", true);
			return ActionResult.PASS;
		});
	}
	private void registerCustomCommands() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			ModCommand.register(dispatcher);
		}));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			HorseBreedCommand.register(dispatcher);
		});
	}
	private static void setModStart() {
		StopwatchHandler.forNotStarted.start();
		RacingHandler.init();
	}
	private static void setModStop() {
		StopwatchHandler.stopAll();
		RacingHandler.init();
	}
	private void addAbilityIfNotPresent(PlayerEntity player, AbstractHorseEntity horse) {
		// map 에서 houseAbility 가 있으면 가져오고 없으면 생성
		HorseAbility horseAbility = HorseAbilityHandler.getOrAddHorseAbility(player.getUuid(), horse.getUuid());
		Cross.LOGGER.info("말 능력치 : " + horseAbility);
	}
}