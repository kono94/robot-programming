package utils;

import components.Drivable;
import components.MyColorSensor;
import lejos.hardware.Button;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;
import lib.Controller;

public class FollowLineController {

    private Normalizer normalizer;
    private Normalizer secondaryNormalizer;
    private Drivable drivable;
    private MyColorSensor colorSensor;
    private Adjuster lineAdjuster;
    private MyColorSensor secondaryColorSensor;


    public FollowLineController(Drivable drivable, MyColorSensor colorSensor, MyColorSensor secondaryColorSensor) {
        this.drivable = drivable;
        this.colorSensor = colorSensor;
        this.secondaryColorSensor = secondaryColorSensor;
    }

    public void init() {
        /*
        colorSensor.switchToRedMode();
        secondaryColorSensor.switchToRedMode();
        */
        System.out.println("Measuring dark color... waiting for click");
        TwoColors darkColor = measureCurrentColorOnClick(colorSensor, secondaryColorSensor);
        System.out.println("darkColor: " + darkColor.primary + " " + darkColor.secondary);
        Delay.msDelay(1000);
        System.out.println("Measuring light color... waiting for click");
        TwoColors lightColor = measureCurrentColorOnClick(colorSensor, secondaryColorSensor);
        System.out.println("lightColor: " + lightColor.primary + " " + lightColor.secondary);
        Delay.msDelay(1000);

        /*

        float darkColor = 0.05f;
        float lightColor = 0.5f;
        */
        normalizer = new Normalizer(darkColor.primary, lightColor.primary, -1, 1);
        secondaryNormalizer = new Normalizer(darkColor.secondary, lightColor.secondary, -1, 1);
        PIDController pid = new PIDController(0);
        pid.setKp(65f);
        pid.setKi(0.1f);
        pid.setKd(8000);
        pid.setMaxIntegral(50);
        pid.setMinIntegral(-50);

        lineAdjuster = pid;

        //lineAdjuster = new SimplePID(0, 80, 50, 40);
        //lineAdjuster = new RegressionAdjuster(normalizer.getMin(), normalizer.getMax());
    }

    private TwoColors measureCurrentColorOnClick(MyColorSensor primay, MyColorSensor secondary) {
        Button.DOWN.waitForPressAndRelease();
        return new TwoColors(primay.getCurrentRedValue(), secondary.getCurrentRedValue());
    }

    public void start() {
        System.out.println("Starting follow line mechanic");
        new Thread(() -> {
            while (Controller.RUN) {
                int turn;
                if (drivable.getSpeed() > 0) {
                    turn = lineAdjuster.calculateAdjustment(normalizer.normalizeValue(colorSensor.getCurrentRedValue()));
                } else {
                    turn = -lineAdjuster.calculateAdjustment(secondaryNormalizer.normalizeValue(secondaryColorSensor.getCurrentRedValue()));
                }
                drivable.drive(turn);
            }
        }).start();
    }


    private class TwoColors {
        float primary;
        float secondary;

        TwoColors(float primary, float secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }
    }
}

