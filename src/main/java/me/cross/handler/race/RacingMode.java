package me.cross.handler.race;

public enum RacingMode {
    NOT_STARTED("NOT_STARTED"),
    READY_FOR_RUNNING("READY_FOR_RUNNING"),
    COUNTDOWN("COUNTDOWN"),
    RUNNING("RUNNING"),
    FINISHED("FINISHED");

    public final String name;

    RacingMode(String name) {
        this.name = name;
    }
}
