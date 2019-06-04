package de.scr.ev3;

import de.scr.Controller;
import de.scr.config.RunControl;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.DriveRemote;
import de.scr.ev3.components.MyGyroSensor;
import lejos.hardware.port.Port;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class ResourceManagerRemote extends ResourceManagerLocal {
    private static Logger logger = LoggerFactory.getLogger(ResourceManagerRemote.class);
    private RemoteEV3 ev3;
    private List<RMIRegulatedMotor> regulatedMotors;

    public ResourceManagerRemote(RemoteEV3 ev3) {
        logger.info("Creating ResourceManagerRemote");
        this.ev3 = ev3;
        regulatedMotors = new ArrayList<>();
        sensors = new ArrayList<>();
        freeResourcesOnShutdown();
    }

    @Override
    public Drivable createDrivable(Port motorA, Port motorB) {
        logger.debug("Creating drivable on {} and {}", motorA.getName(), motorB.getName());
        return new DriveRemote(createRegulatedMotor(motorA), createRegulatedMotor(motorB));
    }

    private RMIRegulatedMotor createRegulatedMotor(Port port) {
        RMIRegulatedMotor motor = ev3.createRegulatedMotor(port.getName(), 'L');
        regulatedMotors.add(motor);
        return motor;
    }

    @Override
    public MyGyroSensor createGyroSensor(Port port) {
        logger.debug("Creating Gyro Sensor");
        MyGyroSensor myGyroSensor = new MyGyroSensor(ev3, port, "Angle");
        sensors.add(myGyroSensor.getCloseable());
        return myGyroSensor;
    }

    private void freeResourcesOnShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Thread size: {}", Thread.getAllStackTraces().size());
            logger.info("Shutdown: Starting Shutdown-Hook");
            Controller.RUN = RunControl.STOP;
            try {
                Thread.sleep(1000);
                for (RMIRegulatedMotor regulatedMotor : regulatedMotors) {
                    regulatedMotor.close();
                }
                logger.info("Showdown: Closed RMIRegulatedMotor");
                for (Closeable sensor : sensors) {
                    sensor.close();
                }
                logger.info("Showdown: Closed Sensors");
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("Shutdown: Finished Shutdown");
        }));
    }

    public List<RMIRegulatedMotor> getRegulatedMotors() {
        return regulatedMotors;
    }

    public List<Closeable> getSensors() {
        return sensors;
    }
}
