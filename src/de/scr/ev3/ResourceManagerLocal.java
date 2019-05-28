package de.scr.ev3;

import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.Drive;
import de.scr.ev3.components.MyGyroSensor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class ResourceManagerLocal implements ResourceManager{
    private static Logger logger = LoggerFactory.getLogger(ResourceManagerLocal.class);
    protected List<Closeable> sensors;

    public ResourceManagerLocal(){
        logger.info("Create Local ResourceManager");
        sensors = new ArrayList<>();
    }

    public EV3UltrasonicSensor createDistanceSensor(Port port){
        logSensor("Distance", port);
        EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(port);
        sensor.setCurrentMode("Distance");
        sensors.add(sensor);
        return sensor;
    }

    public EV3TouchSensor createTouchSensor(Port port){
        logSensor("Touch", port);
        EV3TouchSensor sensor = new EV3TouchSensor(port);
        sensors.add(sensor);
        return sensor;
    }

    public EV3ColorSensor createColorSensor(Port port){
        logSensor("Color", port);
        EV3ColorSensor colorSensor = new EV3ColorSensor(port);
        sensors.add(colorSensor);
        return colorSensor;
    }

    public MyGyroSensor createGyroSensor(Port port) {
        logSensor("Gyro", port);
        MyGyroSensor gyroSensor = new MyGyroSensor(port);
        sensors.add(gyroSensor.getCloseable());
        return gyroSensor;
    }

    private void logSensor(String name, Port port) {
        logger.debug("Create {} Sensor on Port {}", name, port.getName());
    }

    public Drivable createDrivable(Port motorA, Port motorB){
        return new Drive(createLargeRegulatedMotor(motorA), createLargeRegulatedMotor(motorB));
    }

    public EV3LargeRegulatedMotor createLargeRegulatedMotor(Port port){
        System.out.println(port.getName());
        EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(port);
        sensors.add(motor);
        return motor;
    }

    public List<Closeable> getSensors() {
        return sensors;
    }
}
