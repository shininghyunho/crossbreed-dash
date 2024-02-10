package me.cross.entity;

import me.cross.Cross;

import java.util.Timer;
import java.util.TimerTask;

public class RacingTimer {
    private static final Timer timer = new Timer();
    private static TimerTask task;
    private static long time = 0;

    public static void start() {
        // 타이머가 이미 실행중이면 중지
        if(task!=null) task.cancel();

        task = new TimerTask() {
            @Override
            public void run() {
                time++;
                Cross.LOGGER.info("time : " + time);
            }
        };

        timer.schedule(task, 0, 1000);
    }

    public static void stop() {
        if(task!=null) task.cancel();
    }
}
