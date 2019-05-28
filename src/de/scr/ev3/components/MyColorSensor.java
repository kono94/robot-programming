package de.scr.ev3.components;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class MyColorSensor {
    private EV3ColorSensor colorSensor;
    private SensorMode redMode;
    private float[] colorFetch;

    public MyColorSensor(EV3ColorSensor colorSensor){
        this.colorSensor = colorSensor;
        this.redMode = colorSensor.getRedMode();
        this.colorFetch = new float[1];
    }

    public float getCurrentRedValue(){
        redMode.fetchSample(colorFetch, 0);
        return colorFetch[0];
    }

    public void switchToRedMode(){
        colorSensor.setCurrentMode(colorSensor.getRedMode().getName());
    }
}
