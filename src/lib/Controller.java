package lib;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

import java.io.Closeable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Controller {

    public Controller() {
    }

    public ResourceManager setupRemoteResourceManager() throws RemoteException, NotBoundException, MalformedURLException {
        RemoteEV3 ev3 = new RemoteEV3("192.168.0.222");
        ev3.setDefault();

        ResourceManager rm = new ResourceManager(ev3);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                for (RMIRegulatedMotor regulatedMotor : rm.getRegulatedMotors()) {
                    regulatedMotor.close();
                }
                for (Closeable sensor : rm.getSensors()) {
                    sensor.close();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }));

        return rm;
    }

    public void setBackupShutdown(int seconds){
        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }).start();
    }

}
