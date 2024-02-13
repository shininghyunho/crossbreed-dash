package me.cross.handler;

public enum RacingMode {
    NOT_STARTED("NOT_STARTED"),
    READY_FOR_RUNNING("READY_FOR_RUNNING"),
    COUNTDOWN("COUNTDOWN"),
    RUNNING("RUNNING"),
    FINISHED("FINISHED");

    final String name;

    RacingMode(String name) {
        this.name = name;
    }
}
