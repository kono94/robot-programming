package de.scr.logic;

import de.scr.Controller;
import de.scr.config.Constants;
import de.scr.config.RunControl;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyDistanceSensor;
import de.scr.logic.adjuster.SimplePID;
import lejos.utility.Delay;

public class ConvoyController {
    private MyDistanceSensor distanceSensor;
    private Drivable drivable;
    private SimplePID distanceAdjuster;
    private Controller controller;

    public ConvoyController(Controller controller, Drivable drivable, MyDistanceSensor distanceSensor) {
        this.controller = controller;
        this.drivable = drivable;
        this.distanceSensor = distanceSensor;
    }

    public void init() {
        distanceAdjuster = new SimplePID(0.25f, 800, 0, 0);
//        distanceAdjuster.setMaxAdjustment(50); //TODO: TEST THIS
    }

    public void start(Object lock) {
        System.out.println("Starting follow space keeper mechanic");
        new Thread(() -> {
            while (controller.RUN != RunControl.STOP) {
                switch (controller.RUN) {
                    case LINE_CONVOY:


                        float distanceValue = distanceSensor.getCurrentDistance();
                        System.out.printf("Distance Value: %f", distanceValue);

                        if (Float.isFinite(distanceValue)) {
                            int speed = distanceAdjuster.calculateAdjustment(distanceValue);
                            System.out.println("SPEED: " + speed);
                            drivable.setSpeed((speed * Constants.DEFAULT_SPEED) / 100);
                            Delay.msDelay(500);
                        }
                        break;
                    default:
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
