package me.cross.handler;

public class RacingHandler {
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
