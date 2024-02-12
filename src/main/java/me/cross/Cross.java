package me.cross;

import me.cross.entity.HorseAbility;
import me.cross.handler.HorseOwnerHandler;
import me.cross.custom_event.horse.HorseBondWithPlayerCallback;
import me.cross.handler.RacingStopwatch;
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
    public static final Logger LOGGER = LoggerFactory.getLogger("cross");
	private static long prevTime = 0;

	@Override
	public void onInitialize() {
		registerEvents();
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
			RacingStopwatch.start();
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			LOGGER.info("stopwatch stop");
			RacingStopwatch.stop();
		});

		// for every tick
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			long time=RacingStopwatch.getTime();
			// 동기화 문제로 인해 같은 시간에 두번 호출되는 것을 방지
			if(prevTime==time) return;
			prevTime=time;

			sendStopwatchMessage(time, server);
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
	// TODO : 타이머 시간 변경
	private void sendStopwatchMessage(long time, MinecraftServer server) {
		if(time%10==0) sendMessage("time : " + time, server);
		else if(time==1) sendMessage("땡땡땡 이벤트 시작", server);
	}
	private void sendMessage(String message, MinecraftServer server) {
		PlayerManager playerManager = server.getPlayerManager();
		Text text = Text.of(message);
		playerManager.broadcast(text, false);
	}
}