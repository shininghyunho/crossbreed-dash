package me.cross;

import me.cross.custom.CustomBlock;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.custom.event.race.RacingCallback;
import me.cross.entity.HorseAbility;
import me.cross.handler.HorseOwnerHandler;
import me.cross.handler.RacingHandler;
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

import java.util.Timer;
import java.util.TimerTask;

public class Cross implements ModInitializer {
	public static final String MOD_ID = "cross";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final long RACING_INTERVAL_SEC = 30;
	private static final Stopwatch stopwatchForRacingReady = new Stopwatch(RACING_INTERVAL_SEC, Stopwatch.RACING_MOD.READY_FOR_RACING);

	@Override
	public void onInitialize() {
		registerEvents();
		CustomBlock.registerCustomBlock();
		LOGGER.info("Hello Fabric world!");
	}

	private void registerEvents() {
		// Register the ServerMixin class to the server
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			BlockState blockState = world.getBlockState(pos);
			if(blockState.isToolRequired() && !player.isSpectator() && player.getMainHandStack().isEmpty()) {
				LOGGER.info("블록 파괴 시도 이벤트 발생");
			}
			return ActionResult.PASS;
		});

		// HorseBondWithPlayerCallback.EVENT.register
		HorseBondWithPlayerCallback.EVENT.register((player, horse) -> {
			LOGGER.info("말과 상호작용 이벤트 발생");
			addAbility(player, horse);
			return ActionResult.PASS;
		});

		// server start, stop
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LOGGER.info("stopwatch start");
			stopwatchForRacingReady.start();
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			LOGGER.info("stopwatch stop");
			stopwatchForRacingReady.stop();
		});

		// for every tick
		ServerTickEvents.END_SERVER_TICK.register(server -> {
		});

		// 레이스 준비.
		RacingCallback.READY.register(() -> {
			LOGGER.info("RacingCallback.START event");
			// race ready
			RacingHandler.ready();
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
	private void broadcast(String message, MinecraftServer server) {
		PlayerManager playerManager = server.getPlayerManager();
		Text text = Text.of(message);
		playerManager.broadcast(text, false);
	}
}