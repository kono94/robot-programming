package lib;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {

    private RemoteEV3 ev3;
    private List<RMISampleProvider> sampleProviders;
    private List<RMIRegulatedMotor> regulatedMotors;

    ResourceManager(RemoteEV3 ev3){
        sampleProviders = new ArrayList<>();
        regulatedMotors = new ArrayList<>();
        this.ev3 = ev3;
    }

    public RMISampleProvider createDistanceSensor(Port port){
        RMISampleProvider sensor = ev3.createSampleProvider(port.getName(), "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
        sampleProviders.add(sensor);
        return sensor;
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

    public List<RMISampleProvider> getSampleProviders() {
        return sampleProviders;
    }

    public List<RMIRegulatedMotor> getRegulatedMotors() {
        return regulatedMotors;
    }
}
