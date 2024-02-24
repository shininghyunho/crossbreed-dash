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