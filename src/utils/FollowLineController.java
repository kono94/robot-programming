package utils;

import components.Drivable;
import components.MyColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lib.Controller;

public class FollowLineController {

    private Normalizer normalizer;
    private Drivable drivable;
    private MyColorSensor colorSensor;
    private EV3TouchSensor touchSensor;
    private Adjuster lineAdjuster;

    public FollowLineController(Drivable drivable, MyColorSensor colorSensor, EV3TouchSensor touchSensor) {
        this.drivable = drivable;
        this.colorSensor = colorSensor;
        this.touchSensor = touchSensor;
    }

    public void init(){
/*
        System.out.println("Measuring dark color... waiting for click");
        float darkColor = measureCurrentColorOnClick();
        System.out.println("darkColor: " + darkColor);
        Delay.msDelay(1000);
        System.out.println("Measuring light color... waiting for click");
        float lightColor = measureCurrentColorOnClick();
        System.out.println("lightColor: " + lightColor);
        Delay.msDelay(1000);

*/
        float darkColor = 0.05f;
        float lightColor = 0.45f;
        normalizer = new Normalizer(darkColor, lightColor, -1, 1);
        lineAdjuster = new PIDController(0);
        //lineAdjuster = new RegressionAdjuster(normalizer.getMin(), normalizer.getMax());
        colorSensor.switchToRedMode();
    }


    private float measureCurrentColorOnClick(){
        float color = -1;
        float[] touchFetch = new float[1];
        do {
            touchSensor.fetchSample(touchFetch, 0);
            if (touchFetch[0] == 1) {
                color = colorSensor.getCurrentRedValue();
            }
        }while(color == -1);

        return color;
    }

    public void start(){
        System.out.println("Starting follow line mechanic");
        new Thread(() -> {
            while(Controller.RUN){
                int turn = lineAdjuster.calculateAdjustment(normalizer.normalizeValue(colorSensor.getCurrentRedValue()));
                drivable.drive(15, turn);
            }
        }).start();
    }
}

