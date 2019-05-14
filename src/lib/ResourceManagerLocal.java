package lib;

import components.Drivable;
import components.Drive;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class ResourceManagerLocal implements ResourceManager{
    private List<Closeable> peripherals;

    public ResourceManagerLocal(){
        peripherals = new ArrayList<>();
    }

    public EV3UltrasonicSensor createDistanceSensor(Port port){
        EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(port);
        sensor.setCurrentMode("Distance");
        peripherals.add(sensor);
        return sensor;
    }

    public EV3TouchSensor createTouchSensor(Port port){
        EV3TouchSensor sensor = new EV3TouchSensor(port);
        peripherals.add(sensor);
        return sensor;
    }

    public EV3ColorSensor createColorSensor(Port port){
        EV3ColorSensor colorSensor = new EV3ColorSensor(port);
        peripherals.add(colorSensor);
        return colorSensor;
    }

    public EV3GyroSensor createGyroSensor(Port port) {
        EV3GyroSensor gyroSensor = new EV3GyroSensor(port);
        peripherals.add(gyroSensor);
        return gyroSensor;
    }

    public Drivable createDrivable(Port motorA, Port motorB){
        return new Drive(createLargeRegulatedMotor(motorA), createLargeRegulatedMotor(motorB));
    }

    public EV3LargeRegulatedMotor createLargeRegulatedMotor(Port port){
        System.out.println(port.getName());
        EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(port);
        peripherals.add(motor);
        return motor;
    }

    public List<Closeable> getPeripherals() {
        return peripherals;
    }
}
