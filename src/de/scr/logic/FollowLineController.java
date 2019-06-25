package de.scr.logic;

import de.scr.Controller;
import de.scr.config.Constants;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyColorSensor;
import de.scr.logic.adjuster.Adjuster;
import de.scr.logic.adjuster.PIDController;
import de.scr.utils.Normalizer;
import de.scr.utils.RunControl;
import de.scr.utils.TwoColors;
import lejos.hardware.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for following a line with the EV3-Color-Sensor
 */
public class FollowLineController {
    private static Logger logger = LoggerFactory.getLogger(FollowLineController.class);
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
        colorSensor.switchToRedMode(); //TODO:
        secondaryColorSensor.switchToRedMode();
        */

        TwoColors darkColor = measureCurrentColorOnClick(colorSensor, secondaryColorSensor, "dark");
        TwoColors lightColor = measureCurrentColorOnClick(colorSensor, secondaryColorSensor, "light");
        controller.setDarkColor(darkColor);
        controller.setLightColor(lightColor);

        /*

        float darkColor = 0.05f;
        float lightColor = 0.5f;
        */
        normalizer = new Normalizer(darkColor.primary, lightColor.primary, -1, 1);
        secondaryNormalizer = new Normalizer(darkColor.secondary, lightColor.secondary, -1, 1);

        lineAdjuster = buildPidController();

        //lineAdjuster = new SimplePID(0, 80, 50, 40);
        //lineAdjuster = new RegressionAdjuster(normalizer.getMin(), normalizer.getMax());
    }

    private TwoColors measureCurrentColorOnClick(MyColorSensor primary, MyColorSensor secondary, String color) {
        System.out.printf("Measuring %s color... waiting for click\n", color);
        Button.DOWN.waitForPressAndRelease();
        TwoColors twoColors = new TwoColors(primary.getCurrentRedValue(), secondary.getCurrentRedValue());
        System.out.println("lightColor: Primary:" + twoColors.primary + " Secondary:" + twoColors.secondary);
        return twoColors;
    }

    private PIDController buildPidController() {
        PIDController pid = new PIDController(0);
        pid.setKp(65f);
        pid.setKi(0.1f);
        pid.setKd(8000);
        pid.setMaxIntegral(50);
        pid.setMinIntegral(-50);
        return pid;
    }

    public void start(Object lock) {
        logger.info("Starting follow line mechanic");
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

    private void detectLine(RunControl controlOnLine) {
        float f = normalizer.normalizeValue(colorSensor.getCurrentRedValue());
        if (f < 0.2f) {
            logger.info("FOUND LINE - CURRENT VALUE: {}", f);
            drivable.drive(0, 0);
            drivable.setSpeed(Constants.DEFAULT_SPEED);
            controller.changeRunControl(controlOnLine);
        }
    }
}

