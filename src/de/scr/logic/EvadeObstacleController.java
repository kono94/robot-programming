package de.scr.logic;

import de.scr.Controller;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyColorSensor;
import de.scr.ev3.components.MyDistanceSensor;
import de.scr.ev3.components.MyGyroSensor;
import de.scr.utils.RunControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvadeObstacleController {
    private static Logger logger = LoggerFactory.getLogger(EvadeObstacleController.class);
    private static final double EVADE_THRESH_HOLD = 0.12;
    private Drivable drivable;
    private MyGyroSensor gyroSensor;
    private MyDistanceSensor distanceSensor;
    private MyColorSensor colorSensor;
    private Controller controller;

    public EvadeObstacleController(Controller controller, Drivable drivable, MyGyroSensor gyroSensor, MyDistanceSensor distanceSensor, MyColorSensor colorSensor) {
        this.controller = controller;
        this.drivable = drivable;
        this.gyroSensor = gyroSensor;
        this.distanceSensor = distanceSensor;
        this.colorSensor = colorSensor;
    }

    public void init() {
        logger.info("init evading");
    }

    public void start(Object lock) {
        logger.info("start evading");
        new Thread(() -> {
            while (controller.RUN != RunControl.STOP) {
                switch (controller.RUN) {
                    case LINE_EVADE:
                    case LINEDETECT_EVADING:
                        sleepFor(100); //TODO: DO WE REALY NEED THIS?

                        //If Obstacle gets to close, start evade script
                        if (distanceSensor.getCurrentDistance() < EVADE_THRESH_HOLD) {
                            controller.changeRunControl(RunControl.EVADING);
                        }
                        break;
                    case EVADING:
                        logger.debug("Start Rotating");
                        drivable.rotateOnPlace(25, -90, gyroSensor, false);
                        logger.debug("Done with Rotating");

                        logger.debug("Start driving a circle");
                        drivable.drive(drivable.getSpeed() + 10, -40);

                        controller.changeRunControl(RunControl.LINEDETECT_EVADING);
                        break;
                    default:
                        logger.debug("This thread waits...");
                        try {
                            synchronized (lock) {
                                lock.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        }).start();
    }

    private void sleepFor(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
