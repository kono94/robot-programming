package lib;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    private RemoteEV3 ev3;
    private List<RMIRegulatedMotor> regulatedMotors;
    private List<Closeable> sensors;

    ResourceManager(RemoteEV3 ev3){
        regulatedMotors = new ArrayList<>();
        sensors = new ArrayList<>();
        this.ev3 = ev3;
    }

    public EV3UltrasonicSensor createDistanceSensor(Port port){
        EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(port);
        sensor.setCurrentMode("Distance");
        sensors.add(sensor);
        return sensor;
    }

    public EV3ColorSensor createColorSensor(Port port){
        EV3ColorSensor colorSensor = new EV3ColorSensor(port);
        sensors.add(colorSensor);
        return colorSensor;
    }

    public RMIRegulatedMotor createRegularMotor(Port port){
        System.out.println(port.getName());
        RMIRegulatedMotor motor = ev3.createRegulatedMotor(port.getName(), 'L');
        regulatedMotors.add(motor);
        return motor;
    }

    public RemoteEV3 getEv3() {
        return ev3;
    }

    public void setEv3(RemoteEV3 ev3) {
        this.ev3 = ev3;
    }

    public List<RMIRegulatedMotor> getRegulatedMotors() {
        return regulatedMotors;
    }

    public List<Closeable> getSensors() {
        return sensors;
    }
}
