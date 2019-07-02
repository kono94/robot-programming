package de.scr.logic;

import de.scr.Controller;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyDistanceSensor;
import de.scr.ev3.components.MyGyroSensor;
import de.scr.utils.RunControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for evading a close obstacle.
 * Requires the FollowLineController.
 */
public class EvadeObstacleController implements RoutineController {
    private static Logger logger = LoggerFactory.getLogger(EvadeObstacleController.class);
    private static final double EVADE_THRESH_HOLD = 0.12;
    private Drivable drivable;
    private MyGyroSensor gyroSensor;
    private MyDistanceSensor distanceSensor;
    private Controller controller;

    public EvadeObstacleController(Controller controller, Drivable drivable, MyGyroSensor gyroSensor, MyDistanceSensor distanceSensor) {
        this.controller = controller;
        this.drivable = drivable;
        this.gyroSensor = gyroSensor;
        this.distanceSensor = distanceSensor;
    }

    @Override
    public void init() {
        logger.info("init evading");
    }

    @Override
    public void start(Object lock) {
        logger.info("Starting Evade-Controller");
        new Thread(() -> {
            while (controller.RUN != RunControl.STOP) {
                switch (controller.RUN) {
                    case LINE_EVADE:
                    case LINEDETECT_EVADING:
                        //If Obstacle gets to close, start evade script
                        if (distanceSensor.getCurrentDistance() < EVADE_THRESH_HOLD) {
                            controller.changeRunControl(RunControl.EVADING);
                        }
                        break;
                    case EVADING:
                        logger.debug("Start Rotating");
                        drivable.rotateOnPlace(27, -90, gyroSensor);
                        logger.debug("Done with Rotating");

                        logger.debug("Start driving a circle");
                        drivable.drive(-40);

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
}
