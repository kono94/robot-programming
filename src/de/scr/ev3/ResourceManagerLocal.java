package de.scr.ev3;

import de.scr.Controller;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.Drive;
import de.scr.ev3.components.MyGyroSensor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

public class ResourceManagerLocal extends ResourceManager {
    public ResourceManagerLocal(Controller controller) {
        super(controller);
        logger.info("Create Local ResourceManager");
    }

    @Override
    public Drivable createDrivable(Port motorA, Port motorB) {
        return new Drive(createLargeRegulatedMotor(motorA), createLargeRegulatedMotor(motorB));
    }

    private EV3LargeRegulatedMotor createLargeRegulatedMotor(Port port) {
        System.out.println(port.getName());
        EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(port);
        sensors.add(motor);
        return motor;
    }

    @Override
    public MyGyroSensor createGyroSensor(Port port) {
        logSensor("Gyro", port);
        MyGyroSensor gyroSensor = new MyGyroSensor(port);
        sensors.add(gyroSensor.getCloseable());
        return gyroSensor;
    }
}
