package programs;

import components.Drivable;
import components.Drive;
import components.DriveRemote;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RMISampleProvider;
import lib.Controller;
import lib.ResourceManager;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TestProgram {
    public TestProgram(){}

    public void startRemote() throws RemoteException, NotBoundException, MalformedURLException {
        Controller controller = new Controller();
        ResourceManager rm = controller.setupRemoteResourceManager();
        Drivable drive = new DriveRemote(rm.createRegularMotor(MotorPort.B), rm.createRegularMotor(MotorPort.C));
       // drive.rotateOnPlace(200, 360);
       // drive.drive(200, 0);

        EV3ColorSensor colorSensor = rm.createColorSensor(SensorPort.S3);
        colorSensor.setCurrentMode(colorSensor.getRedMode().getName());

        SensorMode a = colorSensor.getRedMode();
        int i = 0;
        while(i++ <200){
            float[] b = new float[5];

            a.fetchSample(b, 1);
            System.out.println(b[0] + " " + b[1]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startLocal(){
        Controller controller = new Controller();
    }
}
