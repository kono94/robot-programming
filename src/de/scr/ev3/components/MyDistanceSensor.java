package de.scr.ev3.components;

import lejos.hardware.sensor.EV3UltrasonicSensor;

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