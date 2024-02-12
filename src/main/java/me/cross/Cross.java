package me.cross;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cross implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("cross");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
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
	}
}