package me.cross.handler;

public class RacingHandler {
    public enum RACING_MOD {
        // 경기가 시작되지 않음
        NOT_STARTED,
        // 출발선에서 경주 준비
        READY,
        // 경주 중
        RUNNING,
        // 경주 종료
        FINISHED
    }
    static RACING_MOD racingMod = RACING_MOD.NOT_STARTED;

    public static void ready() {
        racingMod = RACING_MOD.READY;
        // TODO : 출발선 대기 로직
    }

    public static void start() {
        racingMod = RACING_MOD.RUNNING;
        // TODO : 경주 시작 로직
    }
}
