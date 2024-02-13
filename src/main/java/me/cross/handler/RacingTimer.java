package me.cross.handler;

import me.cross.Cross;

import java.util.Timer;
import java.util.TimerTask;

public class RacingTimer {
    private static final Timer timer = new Timer();
    private static TimerTask task;
    private static long nowTime = 0;

    public static void start() {
        if(task!=null) return;
        task = new TimerTask() {
            @Override
            public void run() {
                nowTime++;
                Cross.LOGGER.info("RacingTimer nowTime : " + nowTime);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public static void stop() {
        if(task!=null) task.cancel();
        nowTime = 0;
    }

    public static long getNowTime() {
        return nowTime;
    }
}
