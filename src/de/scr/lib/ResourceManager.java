package de.scr.lib;

import de.scr.components.Drivable;
import de.scr.components.MyGyroSensor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public interface ResourceManager {
    EV3UltrasonicSensor createDistanceSensor(Port port);
    EV3TouchSensor createTouchSensor(Port port);
    EV3ColorSensor createColorSensor(Port port);
    MyGyroSensor createGyroSensor(Port port);
    Drivable createDrivable(Port motorA, Port motorB);
}
