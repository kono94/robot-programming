package de.scr.ev3.components;

import lejos.hardware.sensor.EV3UltrasonicSensor;

/**
 * Small wrapper class to simplify usage of the EV3UltrasonicSensor
 * which is used to determine the current distance to objects in front.
 * This class is used to hold the distance while driving in convoy
 * and detect obstacles in EVADE_OBSTACLE mode.
 */
public class MyDistanceSensor {
    private EV3UltrasonicSensor distanceSensor;
    private float[] distanceFetch;

    public MyDistanceSensor(EV3UltrasonicSensor distanceSensor){
        this.distanceSensor = distanceSensor;
        this.distanceFetch = new float[1];
    }

    public float getCurrentDistance(){
        distanceSensor.fetchSample(distanceFetch, 0);
        return distanceFetch[0];
    }
}