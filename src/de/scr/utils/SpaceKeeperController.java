package de.scr.utils;

import de.scr.components.Drivable;
import de.scr.components.MyDistanceSensor;
import de.scr.config.Constants;
import de.scr.lib.Controller;

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
            while(Controller.RUN){
                float distanceValue = distanceSensor.getCurrentDistance();
                if(!Float.isFinite(distanceValue)) continue;
                int speed = distanceAdjuster.calculateAdjustment(distanceValue);
                System.out.println("SPEED: " + speed);
                drivable.setSpeed((speed * Constants.DEFAULT_SPEED) / 100);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
