package me.cross.mixin;

import me.cross.Cross;
import me.cross.handler.HorseSummonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow @Final private MinecraftServer server;

    // tick
    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if(HorseSummonHandler.isHorseSummoned()) {
            spawnHorse();
            HorseSummonHandler.summonHorseFinished();
            Cross.LOGGER.info("말이 소환되었습니다. 이히힝~!");
        }
    }
    @Unique
    private void spawnHorse() {
        ServerWorld serverWorld = (ServerWorld) (Object) this;

        // 소환 위치
        BlockPos pos = new BlockPos(0, 100, 0);
        // horse entity
        Entity horse = new HorseEntity(EntityType.HORSE, serverWorld);
        horse.refreshPositionAndAngles(pos, 0, 0);
        // 1 마리의 말을 소환
        serverWorld.spawnEntity(horse);
    }
}
