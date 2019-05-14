package utils;

import components.MyDistanceSensor;
import lejos.hardware.sensor.EV3GyroSensor;

public class EvadeObstacleController {
    private EV3GyroSensor gyroSensor;
    private MyDistanceSensor distanceSensor;

    public EvadeObstacleController(EV3GyroSensor gyroSensor, MyDistanceSensor distanceSensor) {
        this.gyroSensor = gyroSensor;
        this.distanceSensor = distanceSensor;
    }
}
