package lib;

import components.Drivable;
import components.MyColorSensor;
import config.Constants;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.remote.ev3.RemoteEV3;
import org.jfree.util.Log;
import utils.FollowLineController;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Controller {

    public static volatile boolean RUN = false;
    private ResourceManager resourceManager;
    private FollowLineController followLineController;
    private Drivable drivable;
    private MyColorSensor primaryColorSensor;
    private EV3TouchSensor primaryTouchSensor;

    private boolean isRemote;

    public Controller(boolean isRemote) {
        this.isRemote = isRemote;
        if(this.isRemote){
            RemoteEV3 ev3 = null;
            try {
                ev3 = new RemoteEV3(Constants.REMOTE_HOST);
                ev3.setDefault();
            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not setup RemoteEV3");
            }
            resourceManager = new ResourceManagerRemote(ev3);
        }else{
            resourceManager = new ResourceManagerLocal();
        }
    }

    public void init(){
        primaryColorSensor = new MyColorSensor(resourceManager.createColorSensor(Constants.COLOR_SENSOR_PORT));
        drivable = resourceManager.createDrivable(Constants.MOTOR_PORT_LEFT, Constants.MOTOR_PORT_RIGHT);
        primaryTouchSensor = resourceManager.createTouchSensor(Constants.TOUCH_SENSOR_PORT);
        Controller.RUN = true;
    }

    public void followLine(){
        followLineController = new FollowLineController(drivable, primaryColorSensor, primaryTouchSensor);
        followLineController.init();
        registerShutdownOnTouchSensorClick();
        followLineController.start();
    }

    public void registerShutdownOnTouchSensorClick(){
        new Thread(() -> {
            float[] b = new float[1];
            while (RUN) {
                primaryTouchSensor.fetchSample(b, 0);
                if (b[0] == 1) {
                    Controller.RUN = false;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Log.error(e);
                }
            }
        }).start();
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
