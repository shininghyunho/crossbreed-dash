package me.cross.handler;

public class RacingHandler {
    public enum MOD {
        // 경기가 시작되지 않음
        NOT_STARTED,
        // 출발선에서 경주 준비
        READY_FOR_RACING,
        READY_FOR_RUNNING,
        // 경주 중
        RUNNING,
        // 경주 종료
        FINISHED
    }
    public static MOD mod = MOD.NOT_STARTED;

    public static void readyRorRacing() {
        mod = MOD.READY_FOR_RACING;
        // TODO : 경주 준비 로직
    }

    public static void readyForRunning() {
        mod = MOD.READY_FOR_RUNNING;
        // TODO : 경주 시작 준비 로직
    }

    public static void start() {
        mod = MOD.RUNNING;
        // TODO : 경주 시작 로직
    }
}
