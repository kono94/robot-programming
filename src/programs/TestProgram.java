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
import org.jfree.util.Log;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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

        if (runRemote) {
            ResourceManager rm;
            rm = controller.setupRemoteResourceManager();
            drive = new DriveRemote(rm.createRegularMotor(MotorPort.B), rm.createRegularMotor(MotorPort.C));
            // drive.rotateOnPlace(200, 360);
            // drive.drive(200, 0);
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

        new Thread(() -> {
            float[] b = new float[1];
            while (RUN) {
                touchSensor.fetchSample(b, 0);
                if (b[0] == 1) {
                    TestProgram.RUN = false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.error(e);
                }
            }
        }).start();

        float[] b = new float[1];
        while (RUN) {
            float redValue = b[0];
            int turn = (int) (-596 * redValue * redValue + 631 * redValue - 93);
            a.fetchSample(b, 0);
            System.out.printf("\r TURN: %d\t RED-SENSOR: %f", turn, redValue);
            //System.out.println(b[0]);
            // System.out.println("Turn: " + (int) (-720.534709 * b[0] * b[0] + 906.9618594 * b[0] - 166.9780533));
            drive.drive(10, turn);
        }
    }
}
