package de.scr.config;

public enum RunControl {
    //TODO: Maybe LIST
    STOP,
    FOLLOW_LINE,
    HOLD_DISTANCE,
    EVADE_OBSTACLE,
    FOLLOW_HOLD,
    FOLLOW_EVADE,
    ;


    public static boolean isEvadeMode(RunControl run) {
        return run.equals(FOLLOW_EVADE) || run.equals(EVADE_OBSTACLE);
    }

    public static boolean isFollowMode(RunControl run) {
        return run.equals(FOLLOW_EVADE) || run.equals(FOLLOW_HOLD) || run.equals(FOLLOW_LINE);
    }

    public static boolean isHoldDistanceMode(RunControl run) {
        return run.equals(HOLD_DISTANCE) || run.equals(FOLLOW_HOLD);
    }
}
