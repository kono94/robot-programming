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

import java.util.concurrent.ThreadLocalRandom;

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

    public void start() {
        logger.info("start evading");
        new Thread(() -> {
            while (Controller.RUN != RunControl.STOP) {
                if(Controller.RUN == RunControl.EVADE_OBSTACLE || Controller.RUN == RunControl.FOLLOW_EVADE) {
                    float distanceValue = distanceSensor.getCurrentDistance();
                    if(distanceValue < 0.1) {
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

                        drivable.drive(20, 0);

                        // drive forward for 100ms if no line is found
                        TwoColors darkColor = controller.getDarkColor();
                        while(colorSensor.getCurrentRedValue() >= darkColor.primary) {
                            logger.debug("line not found, sleeping 100ms while driving");
                            Delay.msDelay(100);
                        }

                        logger.debug("line found, returning with previous speed {} in mode {}", previousSpeed, previousMode);

                        // continue with previous mode
                        drivable.setSpeed(previousSpeed);
                        Controller.RUN = previousMode;
                    }
                }
            }
        }).start();
    }
}
