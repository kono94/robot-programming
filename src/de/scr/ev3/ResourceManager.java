package de.scr.ev3;

import de.scr.Controller;
import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyGyroSensor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Managing the EV3-Sensors for remote and local access
 */
public abstract class ResourceManager {
    List<Closeable> sensors;
    Logger logger = LoggerFactory.getLogger(getClass());
    Controller controller;

    ResourceManager(Controller controller) {
        sensors = new ArrayList<>();
        this.controller = controller;
    }

    public abstract Drivable createDrivable(Port motorA, Port motorB);

    public abstract MyGyroSensor createGyroSensor(Port port);

    public EV3UltrasonicSensor createDistanceSensor(Port port) {
        logSensor("Distance", port);
        EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(port);
        sensor.setCurrentMode("Distance");
        sensors.add(sensor);
        return sensor;
    }

    public EV3TouchSensor createTouchSensor(Port port) {
        logSensor("Touch", port);
        EV3TouchSensor sensor = new EV3TouchSensor(port);
        sensors.add(sensor);
        return sensor;
    }

    public EV3ColorSensor createColorSensor(Port port) {
        logSensor("Color", port);
        EV3ColorSensor colorSensor = new EV3ColorSensor(port);
        sensors.add(colorSensor);
        return colorSensor;
    }

    void logSensor(String name, Port port) {
        logger.debug("Create {} Sensor on Port {}", name, port.getName());
    }

    public List<Closeable> getSensors() {
        return sensors;
    }
}
