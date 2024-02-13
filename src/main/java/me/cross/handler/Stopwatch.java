package me.cross.handler;

import me.cross.Cross;
import me.cross.custom.event.race.RacingCallback;

import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {
    // enum stopwatch mod : ready for racing
    public enum RACING_MOD {
        READY_FOR_RACING,
    }

    /**
     * @param initialTime 초 단위로 설정
     */
    public Stopwatch(long initialTime, RACING_MOD racingMod) {
        this.nowTime = initialTime;
        this.initialTime = initialTime;
        this.racingMod = racingMod;
    }

    private final Timer timer = new Timer();
    private TimerTask task;
    // 1 min
    private long nowTime;
    private final long initialTime;
    private final RACING_MOD racingMod;

    public void start() {
        // 이미 시작되어 있다면 무시
        if(task!=null) return;
        // 새로운 작업 생성
        task = new TimerTask() {
            @Override
            public void run() {
                Cross.LOGGER.info("time : " + nowTime);
                if(nowTime==0) {
                    if(racingMod==RACING_MOD.READY_FOR_RACING) RacingCallback.READY.invoker().interact();
                }
                // 1초 감소하다가 0이 되면 초기화
                nowTime=0>=nowTime?initialTime:nowTime-1;
            }
        };
        // 작업 시작
        timer.schedule(task, 0, 1000);
    }

    public void stop() {
        if(task!=null) task.cancel();
    }

    public void reset() {
        nowTime = initialTime;
    }
}
