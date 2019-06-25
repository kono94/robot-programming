package de.scr.logic;

import de.scr.Controller;
import de.scr.config.Constants;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyDistanceSensor;
import de.scr.logic.adjuster.Adjuster;
import de.scr.logic.adjuster.SimpleDistancePID;
import de.scr.utils.RunControl;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controller to hold the distance with the EV3-Ultrasonic-Sensor.
 */
public class ConvoyController {
    private static Logger logger = LoggerFactory.getLogger(ConvoyController.class);
    private MyDistanceSensor distanceSensor;
    private Drivable drivable;
    private Adjuster distanceAdjuster;
    private Controller controller;

    public ConvoyController(Controller controller, Drivable drivable, MyDistanceSensor distanceSensor) {
        this.controller = controller;
        this.drivable = drivable;
        this.distanceSensor = distanceSensor;
    }

    public void init() {
        distanceAdjuster = new SimpleDistancePID(0.10f, 800, 0, 0);
    }

    public void start(Object lock) {
        logger.info("Starting follow space keeper mechanic");
        new Thread(() -> {
            while (controller.RUN != RunControl.STOP) {
                switch (controller.RUN) {
                    case LINE_CONVOY:
                        float distanceValue = distanceSensor.getCurrentDistance();
                        logger.debug("Distance Value: {}", distanceValue);

                        if (Float.isFinite(distanceValue)) {
                            int speed = distanceAdjuster.calculateAdjustment(distanceValue);
                            logger.debug("SPEED: {}", speed);
                            drivable.setSpeed((speed * Constants.DEFAULT_SPEED) / 100);
                            Delay.msDelay(500);
                        }
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
