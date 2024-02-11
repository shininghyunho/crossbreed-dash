package me.cross.entity;

import me.cross.Cross;

import java.util.Timer;
import java.util.TimerTask;

public class RacingStopwatch {
    private static final long INITIAL_TIME = 20;

    private static final Timer timer = new Timer();
    private static TimerTask task;
    // 1 min
    private static long initialTime = INITIAL_TIME;

    public static void start() {
        if(task!=null) return;

        task = new TimerTask() {
            @Override
            public void run() {
                initialTime--;
                Cross.LOGGER.info("time : " + initialTime);
                if(initialTime==0) {
                    reset();
                }
            }
        };

        timer.schedule(task, 0, 1000);
    }

    public static void stop() {
        if(task!=null) task.cancel();
    }

    public static long getTime() {
        return initialTime;
    }

    public static void reset() {
        initialTime = INITIAL_TIME;
    }
}
