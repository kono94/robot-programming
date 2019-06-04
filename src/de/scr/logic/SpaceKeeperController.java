package de.scr.logic;

import de.scr.Controller;
import de.scr.config.Constants;
import de.scr.config.RunControl;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyDistanceSensor;
import de.scr.logic.adjuster.Adjuster;
import de.scr.logic.adjuster.SimplePID;
import lejos.utility.Delay;

public class SpaceKeeperController {
    private MyDistanceSensor distanceSensor;
    private Drivable drivable;
    private Adjuster distanceAdjuster;

    public SpaceKeeperController(Drivable drivable, MyDistanceSensor distanceSensor) {
        this.drivable = drivable;
        this.distanceSensor = distanceSensor;
    }

    public void init(){
        distanceAdjuster = new SimplePID(0.25f, 800, 0, 0);
    }

    public void start(){
        System.out.println("Starting follow space keeper mechanic");
        new Thread(() -> {
            while(Controller.RUN != RunControl.STOP) {
                if(Controller.RUN == RunControl.HOLD_DISTANCE || Controller.RUN == RunControl.FOLLOW_HOLD) {
                    float distanceValue = distanceSensor.getCurrentDistance();
                    if(!Float.isFinite(distanceValue)) continue;
                    int speed = distanceAdjuster.calculateAdjustment(distanceValue);
                    System.out.println("SPEED: " + speed);
                    drivable.setSpeed((speed * Constants.DEFAULT_SPEED) / 100);
                    Delay.msDelay(500);
                }
            }
        }).start();
    }
}
