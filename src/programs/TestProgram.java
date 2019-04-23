package programs;

import components.Drivable;
import components.Drive;
import components.DriveRemote;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lib.Controller;
import lib.ResourceManager;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.jfree.util.Log;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class TestProgram {
    public static volatile boolean RUN = true;

    public TestProgram() {
    }

    public void start(boolean runRemote) throws RemoteException, NotBoundException, MalformedURLException {
        Controller controller = new Controller();
        EV3ColorSensor colorSensor;
        EV3TouchSensor touchSensor;
        SensorMode a;
        Drivable drive;
//      double min = 0.03;
//      double max = 0.41;


        if (runRemote) {
            ResourceManager rm;
            rm = controller.setupRemoteResourceManager();
            drive = new DriveRemote(rm.createRegularMotor(MotorPort.B), rm.createRegularMotor(MotorPort.C));
            colorSensor = rm.createColorSensor(SensorPort.S2);
            colorSensor.setCurrentMode(colorSensor.getRedMode().getName());
            a = colorSensor.getRedMode();
            touchSensor = rm.createTouchSensor(SensorPort.S1);
        } else {
            drive = new Drive(new EV3LargeRegulatedMotor(MotorPort.B), new EV3LargeRegulatedMotor(MotorPort.C));
            colorSensor = new EV3ColorSensor(SensorPort.S2);
            colorSensor.setCurrentMode(colorSensor.getRedMode().getName());
            a = colorSensor.getRedMode();
            touchSensor = new EV3TouchSensor(SensorPort.S1);
        }

        System.out.println("Click for first color");
        double min = -1;
        double max = -1;
        float[] touchFetch = new float[1];
        float[] colorFetch = new float[1];
        while(min == -1) {
            touchSensor.fetchSample(touchFetch, 0);
            if (touchFetch[0] == 1) {
                a.fetchSample(colorFetch, 0);
                min = colorFetch[0];
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Click for second color");
        while(max == -1){
            touchSensor.fetchSample(touchFetch, 0);
            if (touchFetch[0] == 1) {
                a.fetchSample(colorFetch, 0);
                max = colorFetch[0];
            }
        }

        System.out.println("min: " + min);
        System.out.println("max: " + max);

        new Thread(() -> {
            float[] b = new float[1];
            while (RUN) {
                touchSensor.fetchSample(b, 0);
                if (b[0] == 1) {
                    TestProgram.RUN = false;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Log.error(e);
                }
            }
        }).start();

        float[] b = new float[1];
        while (RUN) {
            a.fetchSample(b, 0);
            float redValue = b[0];

            /*
                0.03 -100
                0.06 -35
                0.07 -30
                0.9 -10
                0.10 -5
                0.16 0
                0.18 0
                0.2 0
                0.25 10
                0.38 30
                0.41 100

                => f(x) = -596 * x^2 + 631 * x - 93

                0.03 => 1. Farbe schwarz
                0.41 => 2. Farbe hellgrau
                Mittelwert => (1.Farbe + 2.Farbe) / 2

                1. Farbe => -100
                2. Farbe => 100
                Mittelwert => 0 +- (Abstand der Farben * X Prozent)

             */

            PolynomialCurveFitter pcf = PolynomialCurveFitter.create(2);
            List<WeightedObservedPoint> p = new ArrayList<>();

            double middle = (min + max) / 2;
            double diff = max -min;
            double ten = diff * 0.1;

            p.add(new WeightedObservedPoint(80, min, -100));
            p.add(new WeightedObservedPoint(80, max, 100));
            p.add(new WeightedObservedPoint(50, middle, 0));
            p.add(new WeightedObservedPoint(50, middle + ten, 0));
            p.add(new WeightedObservedPoint(50, middle - ten, 0));
            p.add(new WeightedObservedPoint(55, max - ten, 30));

            double[] weights = pcf.fit(p);
            int turn = (int) (weights[2] * redValue * redValue + weights[1] * redValue + weights[0]);
            //int turn = (int) (-596 * redValue * redValue + 631 * redValue - 93);
            // System.out.printf("\r TURN: %d\t RED-SENSOR: %f", turn, redValue);
             System.out.println(b[0]);
            drive.drive(10, turn);
        }
    }
}
