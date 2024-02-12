package me.cross;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

public class CrossClient implements ClientModInitializer {
	// 서버 인스턴스는 접근이 불가능하지만 클라이언트 인스턴스는 접근이 가능.
	MinecraftClient client = MinecraftClient.getInstance();
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}