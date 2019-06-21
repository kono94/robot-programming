package de.scr.logic;

import de.scr.Controller;
import de.scr.config.Constants;
import de.scr.config.RunControl;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyColorSensor;
import de.scr.logic.adjuster.Adjuster;
import de.scr.logic.adjuster.PIDController;
import de.scr.utils.Normalizer;
import de.scr.utils.TwoColors;
import lejos.hardware.Button;

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

        System.out.println("Measuring light color... waiting for click");
        lightColor = measureCurrentColorOnClick(colorSensor, secondaryColorSensor);
        System.out.println("lightColor: " + lightColor.primary + " " + lightColor.secondary);

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

    public void start(Object lock) {
        System.out.println("Starting follow line mechanic");
        new Thread(() -> {
            while (controller.RUN != RunControl.STOP) {
                switch (controller.RUN) {
                    case LINE:
                    case LINE_EVADE:
                    case LINE_CONVOY:
                        int turn;
                        if (drivable.getSpeed() > 0) {
                            turn = lineAdjuster.calculateAdjustment(normalizer.normalizeValue(colorSensor.getCurrentRedValue()));
                        } else {
                            turn = lineAdjuster.calculateAdjustment(secondaryNormalizer.normalizeValue(secondaryColorSensor.getCurrentRedValue()));
                        }
                        drivable.drive(-turn);
                        break;
                    case LINEDETECT:
                        detectLine(RunControl.LINE);
                        break;
                    case LINEDETECT_EVADING:
                        detectLine(RunControl.LINE_EVADE);
                        break;
                    default:
                        System.out.println("FollowLine -> SLEEEEEEEP");
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

    private void detectLine(RunControl controlOnLine) {
        float f = normalizer.normalizeValue(colorSensor.getCurrentRedValue());
        if (f < 0.2f) {
            System.out.println("FOUND LINE - CURRENT VALUE:" + f);
            drivable.drive(0, 0);
            drivable.setSpeed(Constants.DEFAULT_SPEED);
            controller.changeRunControl(controlOnLine);
        }
    }
}

