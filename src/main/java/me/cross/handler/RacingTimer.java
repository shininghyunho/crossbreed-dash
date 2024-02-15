package me.cross.handler;

import me.cross.Cross;
import me.cross.custom.event.race.RacingCallback;

import java.util.Timer;
import java.util.TimerTask;

public class RacingTimer {
    private static final Timer timer = new Timer();
    private static TimerTask task;
    private static long MAX_TIME = 60*2; // 2분
    private static long nowTime = 0;

    public static void start() {
        if(task!=null) return;
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
