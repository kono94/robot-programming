package de.scr.logic.adjuster;

public class DifferentialManager {
    private float previousError = 0;

    public DifferentialManager() {
    }

    /**
     * @param y current error value
     * @return proportional adjustment to the existing error
     */
    public float feedAndGet(float y) {
        float dValue = y - previousError;
        previousError = y;
        return dValue;
    }
}
