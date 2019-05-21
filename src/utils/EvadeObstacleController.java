package utils;

import components.MyDistanceSensor;
import components.MyGyroSensor;
import lib.Controller;

public class EvadeObstacleController {
    private MyGyroSensor gyroSensor;
    private MyDistanceSensor distanceSensor;

    public EvadeObstacleController(MyGyroSensor gyroSensor, MyDistanceSensor distanceSensor) {
        this.gyroSensor = gyroSensor;
        this.distanceSensor = distanceSensor;
    }

    public void init() {
        System.out.println("init evading");
    }

    public void start() {
        new Thread(() -> {
            while (Controller.RUN) {
                System.out.println(gyroSensor.getAngle());
            }
        }).start();
    }
}
