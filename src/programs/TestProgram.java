package programs;

import components.Drivable;
import components.Drive;
import components.DriveRemote;
import lejos.hardware.port.MotorPort;
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
        drive.rotateOnPlace(200, 360);
        drive.drive(200, 0);
    }

    public void startLocal(){
        Controller controller = new Controller();
    }
}
