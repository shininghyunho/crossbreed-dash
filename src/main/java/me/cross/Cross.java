package me.cross;

import me.cross.handler.*;
import me.cross.handler.race.RacingHandler;
import me.cross.handler.stopwatch.StopwatchHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cross implements ModInitializer {
	public static final String MOD_ID = "cross";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer server;

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		RegisterHandler.register();
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
	private static void setModStart() {
		StopwatchHandler.forNotStarted.start();
		RacingHandler.init();
	}
	private static void setModStop() {
		StopwatchHandler.stopAll();
		RacingHandler.init();
	}
}