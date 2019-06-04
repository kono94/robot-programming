package de.scr.logic;

import de.scr.Controller;
import de.scr.config.RunControl;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyColorSensor;
import de.scr.logic.adjuster.Adjuster;
import de.scr.logic.adjuster.PIDController;
import de.scr.utils.Normalizer;
import de.scr.utils.TwoColors;
import lejos.hardware.Button;
import lejos.utility.Delay;

public class FollowLineController {

    private Normalizer normalizer;
    private Normalizer secondaryNormalizer;
    private Drivable drivable;
    private MyColorSensor colorSensor;
    private Adjuster lineAdjuster;
    private MyColorSensor secondaryColorSensor;
    private Controller controller;


    public FollowLineController(Controller controller, Drivable drivable, MyColorSensor colorSensor, MyColorSensor secondaryColorSensor) {
        this.controller = controller;
        this.drivable = drivable;
        this.colorSensor = colorSensor;
        this.secondaryColorSensor = secondaryColorSensor;
    }

    public void init() {
        /*
        colorSensor.switchToRedMode();
        secondaryColorSensor.switchToRedMode();
        */
        TwoColors darkColor;
        TwoColors lightColor;

        System.out.println("Measuring dark color... waiting for click");
        darkColor = measureCurrentColorOnClick(colorSensor, secondaryColorSensor);
        System.out.println("darkColor: " + darkColor.primary + " " + darkColor.secondary);
        Delay.msDelay(1000);
        System.out.println("Measuring light color... waiting for click");
        lightColor = measureCurrentColorOnClick(colorSensor, secondaryColorSensor);
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
        controller.setDarkColor(darkColor);
        controller.setLightColor(lightColor);
    }

    private TwoColors measureCurrentColorOnClick(MyColorSensor primary, MyColorSensor secondary) {
        Button.DOWN.waitForPressAndRelease();
        return new TwoColors(primary.getCurrentRedValue(), secondary.getCurrentRedValue());
    }

    public void start() {
        System.out.println("Starting follow line mechanic");
        new Thread(() -> {
            while (Controller.RUN != RunControl.STOP) {
                if (Controller.RUN == RunControl.FOLLOW_LINE || Controller.RUN == RunControl.FOLLOW_HOLD
                        || Controller.RUN == RunControl.FOLLOW_EVADE) {
                    int turn;
                    if (drivable.getSpeed() > 0) {
                        turn = lineAdjuster.calculateAdjustment(normalizer.normalizeValue(colorSensor.getCurrentRedValue()));
                    } else {
                        turn = -lineAdjuster.calculateAdjustment(secondaryNormalizer.normalizeValue(secondaryColorSensor.getCurrentRedValue()));
                    }
                    drivable.drive(turn);
                }
            }
        }).start();
    }
}

