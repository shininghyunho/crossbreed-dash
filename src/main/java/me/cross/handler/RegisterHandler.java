package me.cross.handler;

import me.cross.Cross;
import me.cross.custom.CustomBlock;
import me.cross.custom.command.HorseBreedCommand;
import me.cross.custom.command.ModCommand;
import me.cross.custom.event.horse.HorseBondWithPlayerCallback;
import me.cross.custom.event.race.RacingCallback;
import me.cross.custom.event.race.RacingCountdownTickCallback;
import me.cross.custom.event.race.RacingRemainTimeCallback;
import me.cross.entity.HorseAbility;
import me.cross.handler.horse.HorseAbilityHandler;
import me.cross.handler.race.CheckPointBlockHandler;
import me.cross.handler.race.RacingHandler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public class RegisterHandler {
    public static void register() {
        // custom block
        CustomBlock.registerCustomBlock();

        // events
        registerHorseEvents();
        registerServerEvents();
        registerRacingEvents();

        // commands
        registerCustomCommands();

        // check point block
        CheckPointBlockHandler.initCheckPointBlockPosMap();
    }
    private static void registerHorseEvents() {
        // HorseBondWithPlayerCallback.EVENT.register
        HorseBondWithPlayerCallback.EVENT.register((player, horse) -> {
            Cross.LOGGER.info("말과 상호작용 이벤트 발생");
            // 말 능력치가 없다면 생성
            addAbilityIfNotPresent(player, horse);
        });
    }
    private static void registerServerEvents() {
        // server start, stop
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Cross.LOGGER.info("server start");
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            Cross.LOGGER.info("server stop");
        });
        // for every tick
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if(Cross.server == null) Cross.server = server;
        });
    }
    private static void registerRacingEvents() {
        // 달리기 준비.
        RacingCallback.READY_FOR_RUNNING.register(() -> {
            Cross.LOGGER.info("달리기 준비 이벤트");
            RacingHandler.readyForRunning();
            return ActionResult.PASS;
        });

        // 카운트다운
        RacingCallback.COUNTDOWN.register(() -> {
            Cross.LOGGER.info("카운트다운 이벤트");
            RacingHandler.countdown();
            return ActionResult.PASS;
        });

        // Running
        RacingCallback.RUNNING.register(() -> {
            Cross.LOGGER.info("달리기 이벤트");
            RacingHandler.run();
            return ActionResult.PASS;
        });

        // Finished
        RacingCallback.FINISHED.register(() -> {
            Cross.LOGGER.info("경주 종료 이벤트");
            RacingHandler.finished();
            return ActionResult.PASS;
        });

        // END
        RacingCallback.END.register(() -> {
            Cross.LOGGER.info("경주 완전 종료 이벤트, 다음 경기 준비");
            RacingHandler.end();
            return ActionResult.PASS;
        });

        // 카운트다운 틱
        RacingCountdownTickCallback.COUNTDOWN_TICK.register((nowTime) -> {
            MessageHandler.broadcast("카운트다운 : " + nowTime + "초", true);
            return ActionResult.PASS;
        });

        // 경시 시작 전 남은 시간
        RacingRemainTimeCallback.REMAIN_TIME.register((nowTime) -> {
            MessageHandler.broadcast("경주 시작까지 " + secToRealTime(nowTime) + " 남았습니다.", true);
            return ActionResult.PASS;
        });
    }
    private static void registerCustomCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            ModCommand.register(dispatcher);
        }));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            HorseBreedCommand.register(dispatcher);
        });
    }
    private static void addAbilityIfNotPresent(PlayerEntity player, AbstractHorseEntity horse) {
        // map 에서 houseAbility 가 있으면 가져오고 없으면 생성
        HorseAbility horseAbility = HorseAbilityHandler.getOrAddHorseAbility(player.getUuid(), horse.getUuid());
        Cross.LOGGER.info("말 능력치 : " + horseAbility);
    }
    // sec to real time
    private static String secToRealTime(long sec) {
        long hour = sec / 3600;
        long min = (sec % 3600) / 60;
        long second = sec % 60;
        // if hour,min is 0, don't show
        if(hour==0) {
            if(min==0) {
                return second + "초";
            }
            return min + "분 " + second + "초";
        }
        return hour + "시간 " + min + "분 " + second + "초";
    }
}
