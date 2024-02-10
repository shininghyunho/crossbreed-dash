package me.cross.mixin;

import me.cross.Cross;
import me.cross.entity.RacingTimer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class ServerMixin {
    // 서버가 로드되면 레이싱 타이머를 시작
    @Inject(at = @At("TAIL"), method = "loadWorld")
    private void loadWorld(CallbackInfo ci) {
        Cross.LOGGER.info("loadWorld");
        RacingTimer.start();
    }

    // 서버가 정지 상태면 레이싱 타이머를 중지
    @Inject(at = @At("TAIL"), method = "stop")
    private void stop(CallbackInfo ci) {
        Cross.LOGGER.info("stop");
        RacingTimer.stop();
    }
}
