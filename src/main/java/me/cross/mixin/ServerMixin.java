package me.cross.mixin;

import me.cross.Cross;
import me.cross.entity.RacingStopwatch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class ServerMixin {
    @Unique
    private static long prevTime = 0;

    // 서버가 로드되면 레이싱 타이머를 시작
    @Inject(at = @At("TAIL"), method = "loadWorld")
    private void loadWorld(CallbackInfo ci) {
        Cross.LOGGER.info("loadWorld");
        RacingStopwatch.start();
    }

    // 서버가 정지 상태면 레이싱 타이머를 중지
    @Inject(at = @At("TAIL"), method = "stop")
    private void stop(CallbackInfo ci) {
        Cross.LOGGER.info("stop");
        RacingStopwatch.stop();
    }

    // tick
    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        sendStopWatchMessage();
    }
    @Unique
    public void sendStopWatchMessage() {
        long time=RacingStopwatch.getTime();
        // 동기화 문제로 인해 같은 시간에 두번 호출되는 것을 방지
        if(prevTime==time) return;
        prevTime=time;

        if(time%10==0) sendMessage("time : " + time);
        else if(time==1) sendMessage("땡땡땡 이벤트 시작");
    }
    @Unique
    public void sendMessage(String message) {
        PlayerManager playerManager = ((MinecraftServer) (Object) this).getPlayerManager();
        Text text = Text.of(message);
        playerManager.broadcast(text, false);
    }
}
