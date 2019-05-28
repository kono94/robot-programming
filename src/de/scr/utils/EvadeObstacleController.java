package de.scr.utils;

import de.scr.components.MyDistanceSensor;
import de.scr.components.MyGyroSensor;
import de.scr.lib.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvadeObstacleController {
    private static Logger logger = LoggerFactory.getLogger(EvadeObstacleController.class);
    private MyGyroSensor gyroSensor;
    private MyDistanceSensor distanceSensor;

    public EvadeObstacleController(MyGyroSensor gyroSensor, MyDistanceSensor distanceSensor) {
        this.gyroSensor = gyroSensor;
        this.distanceSensor = distanceSensor;
    }

    public void init() {
        logger.info("init evading");
    }

    public void start() {
        new Thread(() -> {
            while (Controller.RUN) {
                logger.debug("Sensor Angle: {}", gyroSensor.getAngle());
            }
        }).start();
    }
}
