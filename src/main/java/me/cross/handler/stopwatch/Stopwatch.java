package me.cross.handler.stopwatch;

import me.cross.Cross;
import me.cross.custom.event.race.RacingCallback;
import me.cross.custom.event.race.RacingCountdownTickCallback;
import me.cross.custom.event.race.RacingRemainTimeCallback;
import me.cross.handler.race.RacingMode;

import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {
    /**
     * @param initialTime 초 단위로 설정
     */
    public Stopwatch(long initialTime, RacingMode mode) {
        this.nowTime = initialTime;
        this.initialTime = initialTime;
        this.mode = mode;
    }

    private final Timer timer = new Timer();
    private TimerTask task;
    // 1 min
    private long nowTime;
    private final long initialTime;
    private final RacingMode mode;

    public void start() {
        // 이미 시작되어 있다면 중지
        if(task!=null) {
            stop();
        }
        // 새로운 작업 생성
        task = new TimerTask() {
            @Override
            public void run() {
                Cross.LOGGER.info("Stopwatch mode : " + mode.name + ", nowTime : " + nowTime);
                // countdown
                if(mode == RacingMode.COUNTDOWN) RacingCountdownTickCallback.COUNTDOWN_TICK.invoker().interact(nowTime);
                // NOT_STARTED 에서 30초 마다 broadcast
                if(mode == RacingMode.NOT_STARTED && nowTime%10==0 && nowTime!=0) RacingRemainTimeCallback.REMAIN_TIME.invoker().interact(nowTime);
                if(nowTime==0) {
                    invokeCallbackBasedOnMode();
                    stop();
                }
                nowTime--;
            }
        };
        // 작업 시작
        timer.schedule(task, 0, 1000);
    }

    private void invokeCallbackBasedOnMode() {
        switch (mode) {
            case NOT_STARTED:
                RacingCallback.READY_FOR_RUNNING.invoker().interact();
                break;
            case READY_FOR_RUNNING:
                RacingCallback.COUNTDOWN.invoker().interact();
                break;
            case COUNTDOWN:
                RacingCallback.RUNNING.invoker().interact();
                break;
            case FINISHED:
                RacingCallback.END.invoker().interact();
                break;
        }
    }
    public void pause() {
        if(task!=null) task.cancel();
    }
    public void stop() {
        pause();
        nowTime = initialTime;
    }
}
