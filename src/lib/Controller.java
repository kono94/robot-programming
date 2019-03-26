package lib;

import components.Drivable;
import components.DriveRemote;
import lejos.hardware.port.MotorPort;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.SampleProvider;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class Controller {

    public Controller(){}

    public ResourceManager setupRemoteResourceManager() throws RemoteException, NotBoundException, MalformedURLException {
        RemoteEV3 ev3 = new RemoteEV3("192.168.0.222");
        ev3.setDefault();

        ResourceManager rm = new ResourceManager(ev3);

        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            try{
                for(RMISampleProvider sp: rm.getSampleProviders()){
                    sp.close();
                }
                for(RMIRegulatedMotor regulatedMotor: rm.getRegulatedMotors()){
                    regulatedMotor.close();
                }
            }catch (Exception e){
                System.err.println(e);
            }
        }));

        return rm;
    }
}
