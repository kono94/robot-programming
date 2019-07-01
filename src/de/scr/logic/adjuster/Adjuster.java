package de.scr.logic.adjuster;

/**
 * Classes that are able to calculate an adjustment value so
 * a certain set point is reached.
 * Mainly PID-controller implementations.
 */
public interface Adjuster {
    int calculateAdjustment(float currentSensorValue);
}
