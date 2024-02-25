package me.cross.handler.race;

import me.cross.Cross;

import java.util.Timer;
import java.util.TimerTask;

public class RacingTimer {
    private static final Timer timer = new Timer();
    private static TimerTask task;
    private static long MAX_TIME = 60*2; // 2분
    private static long nowTime = 0;

    public static void start() {
        // 이미 시작되어있다면 return
        stop();

        task = new TimerTask() {
            @Override
            public void run() {
                nowTime++;
                Cross.LOGGER.info("RacingTimer nowTime : " + nowTime);
                if(nowTime>=MAX_TIME) {
                    RunningHandler.setFinished();
                    stop();
                }
            }
        };

        Cross.LOGGER.info("레이싱 타이머를 시작했습니다.");
        timer.schedule(task, 0, 1000);
    }

    public static void stop() {
        if(task!=null) task.cancel();
        nowTime = 0;
    }

    public static Long getTime() {
        return nowTime;
    }
}
