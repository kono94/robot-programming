package programs;

import components.Drivable;
import components.DriveRemote;
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
    public TestProgram(){}

    public void startRemote() throws RemoteException, NotBoundException, MalformedURLException {
        Controller controller = new Controller();
        controller.setBackupShutdown(20);
        ResourceManager rm = controller.setupRemoteResourceManager();
        Drivable drive = new DriveRemote(rm.createRegularMotor(MotorPort.B), rm.createRegularMotor(MotorPort.C));
       // drive.rotateOnPlace(200, 360);
       // drive.drive(200, 0);

        EV3ColorSensor colorSensor = rm.createColorSensor(SensorPort.S3);
        colorSensor.setCurrentMode(colorSensor.getRedMode().getName());

        SensorMode a = colorSensor.getRedMode();


        EV3TouchSensor touchSensor = rm.createTouchSensor(SensorPort.S1);

        new Thread(() -> {
            float[] b = new float[1];
            while(true){
                touchSensor.fetchSample(b, 0);
                if(b[0] == 1){
                    System.exit(0);
                }
                try {
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    Log.error(e);
                }
            }
        }).start();


        float[] b = new float[1];
        while(true){
            a.fetchSample(b, 0);
            System.out.println(b[0]);
            System.out.println("Turn: " + (int) (b[0] * 1000));
            drive.drive(20, (int) (b[0] * 1000));
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startLocal(){
        Controller controller = new Controller();
    }
}
