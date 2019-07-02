package de.scr;

import de.scr.config.Constants;
import de.scr.ev3.ResourceManager;
import de.scr.ev3.ResourceManagerLocal;
import de.scr.ev3.ResourceManagerRemote;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyColorSensor;
import de.scr.ev3.components.MyDistanceSensor;
import de.scr.ev3.components.MyGyroSensor;
import de.scr.logic.*;
import de.scr.utils.RunControl;
import de.scr.utils.TwoColors;
import lejos.remote.ev3.RemoteEV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialize the main components for the ev3-control
 */
public class Controller {
    private static Logger logger = LoggerFactory.getLogger(Controller.class);

    public volatile RunControl RUN = RunControl.STOP;
    private final Object lock = new Object();
    private ResourceManager resourceManager;
    private Drivable drivable;
    private MyColorSensor primaryColorSensor;
    private MyDistanceSensor primaryDistanceSensor;
    private MyColorSensor secondaryColorSensor;
    private MyGyroSensor gyroSensor;
    private TwoColors darkColor;
    private TwoColors lightColor;

    // when true => is running locally on the robot,
    // when false => using RMI
    private boolean isRunningOnDevice;

    public Controller() {
        logger.info("Creating Controller");
        this.isRunningOnDevice = System.getProperty("os.arch").toLowerCase().matches("arm");
        logger.info("EV3 is started {}", isRunningOnDevice ? "on local" : "via remote");
        init();
    }

    private void init() {
        logger.info("Init Controller");
        initResourceManager();
        createEv3Components();
        RUN = Constants.START_MODE;
        logger.info("Current Mode: {}", RUN);

        startSubRoutines();
    }

    private void initResourceManager() {
        logger.info("Initializing ResourceManager");
        if (isRunningOnDevice) {
            logger.info("Connecting local");
            resourceManager = new ResourceManagerLocal(this);
        } else {
            logger.info("Connecting with RMI");
            RemoteEV3 ev3;
            try {
                ev3 = new RemoteEV3(Constants.REMOTE_HOST);
                ev3.setDefault();
            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not setup RemoteEV3");
            }
            resourceManager = new ResourceManagerRemote(this, ev3);
        }
    }

    private void startSubRoutines() {
        List<RoutineController> routineControllers = new ArrayList<>();

        switch (RUN) {
            case LINE_EVADE:
                routineControllers.add(createFollowLineController());
                routineControllers.add(createEvadeObstacleController());
                break;
            case LINE_CONVOY:
                routineControllers.add(createFollowLineController());
                routineControllers.add(createConvoyController());
                break;
            case LINE:
                routineControllers.add(createFollowLineController());
                break;
            case GUI_MODE:
                routineControllers.add(createOdometryController());
                break;
            default:
                logger.warn("{} is not a valid Start-Mode!", RUN);
        }


        for (RoutineController routineController : routineControllers) {
            routineController.init();
            routineController.start(lock);
        }
    }

    public void changeRunControl(RunControl c) {
        this.RUN = c;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void createEv3Components() {
        drivable = resourceManager.createDrivable(Constants.MOTOR_PORT_LEFT, Constants.MOTOR_PORT_RIGHT);
        primaryColorSensor = new MyColorSensor(resourceManager.createColorSensor(Constants.COLOR_SENSOR_PORT));
        primaryDistanceSensor = new MyDistanceSensor(resourceManager.createDistanceSensor(Constants.DISTANCE_SENSOR_PORT));
        secondaryColorSensor = new MyColorSensor(resourceManager.createColorSensor(Constants.COLOR_SENSOR_2_PORT));
        gyroSensor = resourceManager.createGyroSensor(Constants.GYRO_SENSOR_PORT);
    }

    private RoutineController createFollowLineController() {
        return new FollowLineController(this, drivable, primaryColorSensor, secondaryColorSensor);
    }


    private RoutineController createConvoyController() {
        return new ConvoyController(this, drivable, primaryDistanceSensor);
    }

    private RoutineController createEvadeObstacleController() {
        return new EvadeObstacleController(this, drivable, gyroSensor, primaryDistanceSensor);
    }

    private RoutineController createOdometryController() {
        return new OdometryController(drivable, gyroSensor);
    }

    public TwoColors getDarkColor() {
        return darkColor;
    }

    public TwoColors getLightColor() {
        return lightColor;
    }

    public void setDarkColor(TwoColors darkColor) {
        this.darkColor = darkColor;
    }

    public void setLightColor(TwoColors lightColor) {
        this.lightColor = lightColor;
    }
}
