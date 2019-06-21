package de.scr.logic;

import de.scr.Controller;
import de.scr.config.RunControl;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyColorSensor;
import de.scr.ev3.components.MyDistanceSensor;
import de.scr.ev3.components.MyGyroSensor;
import de.scr.utils.TwoColors;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvadeObstacleController {
    private static Logger logger = LoggerFactory.getLogger(EvadeObstacleController.class);
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
            while (Controller.RUN != RunControl.STOP) {
                if (RunControl.isEvadeMode(Controller.RUN)) {

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    float distanceValue = distanceSensor.getCurrentDistance();
                    if (distanceValue < 0.07) {
                        RunControl previousMode = Controller.RUN;
                        Controller.RUN = RunControl.EVADE_OBSTACLE;
                        int previousSpeed = drivable.getSpeed();

                        // turn right and drive
                        float angleBeforeTurn = gyroSensor.getAngle();
                        drivable.rotateOnPlace(30, -60, gyroSensor);
                        float angleAfterTurn = gyroSensor.getAngle();
                        logger.debug("Before {}, After {}, Diff {}", angleBeforeTurn, angleAfterTurn, angleBeforeTurn - angleAfterTurn);

                        drivable.drive(30, 0);

                        Delay.msDelay(1500);

                        // turn to 0 degree, drive forward
                        angleBeforeTurn = gyroSensor.getAngle();
                        drivable.rotateOnPlace(30, 60, gyroSensor);
                        angleAfterTurn = gyroSensor.getAngle();
                        logger.debug("Before {}, After {}, Diff {}", angleBeforeTurn, angleAfterTurn, angleBeforeTurn - angleAfterTurn);

                        drivable.drive(30, 0);

                        Delay.msDelay(2000);

                        // return to line
                        angleBeforeTurn = gyroSensor.getAngle();
                        drivable.rotateOnPlace(30, 60, gyroSensor);
                        angleAfterTurn = gyroSensor.getAngle();
                        logger.debug("Before {}, After {}, Diff {}", angleBeforeTurn, angleAfterTurn, angleBeforeTurn - angleAfterTurn);

                        drivable.drive(10, 0);

                        // drive forward for 100ms if no line is found
                        TwoColors darkColor = controller.getDarkColor();
                        logger.debug("Trying to find line");
                        while (colorSensor.getCurrentRedValue() >= darkColor.primary) {

                        }

                        drivable.drive(0, 0);

                        logger.debug("line found, returning with previous speed {} in mode {}", previousSpeed, previousMode);

                        // continue with previous mode
                        Controller.RUN = previousMode;
                        drivable.setSpeed(previousSpeed);

                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                } else {
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
}
