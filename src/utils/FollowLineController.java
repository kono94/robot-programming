package utils;

import components.Drivable;
import components.MyColorSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;
import lib.Controller;

public class FollowLineController {

    private Normalizer normalizer;
    private Drivable drivable;
    private MyColorSensor colorSensor;
    private EV3TouchSensor touchSensor;
    private PIDController pid;

    public FollowLineController(Drivable drivable, MyColorSensor colorSensor, EV3TouchSensor touchSensor) {
        this.drivable = drivable;
        this.colorSensor = colorSensor;
        this.touchSensor = touchSensor;
    }

    public void init(){
        System.out.println("Measuring dark color... waiting for click");
        float darkColor = measureCurrentColorOnClick();
        System.out.println("darkColor: " + darkColor);
        Delay.msDelay(1000);
        System.out.println("Measuring light color... waiting for click");
        float lightColor = measureCurrentColorOnClick();
        System.out.println("lightColor: " + lightColor);
        Delay.msDelay(1000);


        this.normalizer = new Normalizer(darkColor, lightColor, -1, 1);
        pid = new PIDController(0);
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
                int turn = pid.calculateAdjustment(normalizer.normalizeValue(colorSensor.getCurrentRedValue()));
                drivable.drive(10, turn);
                System.out.println(turn);
            }
        }).start();
    }
}

