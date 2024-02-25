package me.cross.handler.stopwatch;

import me.cross.handler.race.RacingMode;

public class StopwatchHandler {
    private static final long RACING_INTERVAL_SEC = 5, RUNNING_READY_SEC = 3, COUNTDOWN_SEC = 5, FINISHED_SEC = 10;
    public static final Stopwatch forNotStarted = new Stopwatch(RACING_INTERVAL_SEC, RacingMode.NOT_STARTED);
    public static final Stopwatch forRunningReady = new Stopwatch(RUNNING_READY_SEC, RacingMode.READY_FOR_RUNNING);
    public static final Stopwatch forCountdown = new Stopwatch(COUNTDOWN_SEC, RacingMode.COUNTDOWN);
    public static final Stopwatch forFinished = new Stopwatch(FINISHED_SEC, RacingMode.FINISHED);

    public static void stopAll() {
        StopwatchHandler.forNotStarted.stop();
        StopwatchHandler.forRunningReady.stop();
        StopwatchHandler.forCountdown.stop();
        StopwatchHandler.forFinished.stop();
    }
}
