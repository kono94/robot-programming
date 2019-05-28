package de.scr.lib;

import de.scr.components.Drivable;
import de.scr.components.MyColorSensor;
import de.scr.components.MyDistanceSensor;
import de.scr.components.MyGyroSensor;
import de.scr.config.Constants;
import lejos.hardware.Button;
import lejos.remote.ev3.RemoteEV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.scr.utils.EvadeObstacleController;
import de.scr.utils.FollowLineController;
import de.scr.utils.SpaceKeeperController;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Controller {
    private static Logger logger = LoggerFactory.getLogger(Controller.class);

    public static volatile boolean RUN = false;
    private ResourceManager resourceManager;
    private FollowLineController followLineController;
    private SpaceKeeperController spaceKeeperController;
    private EvadeObstacleController evadeObstacleController;
    private Drivable drivable;
    private MyColorSensor primaryColorSensor;
    private MyDistanceSensor primaryDistanceSensor;
    private MyColorSensor secondaryColorSensor;
    private MyGyroSensor gyroSensor;

    // when true => is running locally on the robot,
    // when false => using RMI
    private boolean isRunningOnDevice;

    public Controller(boolean isRunningOnDevice) {
        logger.info("Creating Controller");
        this.isRunningOnDevice = isRunningOnDevice;
        if (!this.isRunningOnDevice) {
            logger.info("Connecting with RMI");
            RemoteEV3 ev3;
            try {
                ev3 = new RemoteEV3(Constants.REMOTE_HOST);
                ev3.setDefault();
            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not setup RemoteEV3");
            }
            resourceManager = new ResourceManagerRemote(ev3);
        } else {
            logger.info("Connecting local");
            resourceManager = new ResourceManagerLocal();
        }
    }

    public void init() {
        logger.info("Init Controller");
        primaryColorSensor = new MyColorSensor(resourceManager.createColorSensor(Constants.COLOR_SENSOR_PORT));
        drivable = resourceManager.createDrivable(Constants.MOTOR_PORT_LEFT, Constants.MOTOR_PORT_RIGHT);
        primaryDistanceSensor = new MyDistanceSensor(resourceManager.createDistanceSensor(Constants.DISTANCE_SENSOR_PORT));
        secondaryColorSensor = new MyColorSensor(resourceManager.createColorSensor(Constants.COLOR_SENSOR_2_PORT));
        gyroSensor = resourceManager.createGyroSensor(Constants.GYRO_SENSOR_PORT);
        Controller.RUN = true;
    }

    public void followLine() {
        logger.info("Start followLine Mode");
        followLineController = new FollowLineController(drivable, primaryColorSensor, secondaryColorSensor);
        followLineController.init();
        followLineController.start();
    }

    public void holdDistance() {
        logger.info("Start holdDistance Mode");
        spaceKeeperController = new SpaceKeeperController(drivable, primaryDistanceSensor);
        spaceKeeperController.init();
        spaceKeeperController.start();
    }

    public void evadeObstacle() {
        logger.info("Start evadeObstacle Mode");
        evadeObstacleController = new EvadeObstacleController(gyroSensor, primaryDistanceSensor);
        evadeObstacleController.init();
        evadeObstacleController.start();
    }

    public void registerShutdownOnClick() {
        new Thread(() -> {
            Button.UP.waitForPressAndRelease();
            Controller.RUN = false;
        }).start();
    }
}
