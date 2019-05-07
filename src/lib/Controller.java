package lib;

import components.Drivable;
import components.MyColorSensor;
import components.MyDistanceSensor;
import config.Constants;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.remote.ev3.RemoteEV3;
import org.jfree.util.Log;
import utils.FollowLineController;
import utils.SpaceKeeperController;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Controller {

    public static volatile boolean RUN = false;
    private ResourceManager resourceManager;
    private FollowLineController followLineController;
    private SpaceKeeperController spaceKeeperController;
    private Drivable drivable;
    private MyColorSensor primaryColorSensor;
    private EV3TouchSensor primaryTouchSensor;
    private MyDistanceSensor primaryDistanceSensor;
    private MyColorSensor secondaryColorSensor;

    // when true => is running locally on the robot,
    // when false => using RMI
    private boolean isRunningOnDevice;

    public Controller(boolean isRunningOnDevice) {
        this.isRunningOnDevice = isRunningOnDevice;
        if(!this.isRunningOnDevice){
            RemoteEV3 ev3;
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
        primaryDistanceSensor = new MyDistanceSensor(resourceManager.createDistanceSensor(Constants.DISTANCE_SENSOR_PORT));
        secondaryColorSensor = new MyColorSensor(resourceManager.createColorSensor(Constants.COLOR_SENSOR_2_PORT));
        Controller.RUN = true;
    }

    public void followLine(){
        followLineController = new FollowLineController(drivable, primaryColorSensor, primaryTouchSensor, secondaryColorSensor);
        followLineController.init();
        // registerShutdownOnTouchSensorClick();
        followLineController.start();
    }

    public void holdDistance(){
        spaceKeeperController = new SpaceKeeperController(drivable, primaryDistanceSensor);
        spaceKeeperController.init();
        spaceKeeperController.start();
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
