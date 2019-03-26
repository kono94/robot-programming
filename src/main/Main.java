package main;

import components.Drive;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.AnalogSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.sensor.NXTSoundSensor;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {

        RemoteEV3 ev3 = null;
        try {
            ev3 = new RemoteEV3("192.168.0.222");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        ev3.setDefault();
        Sound.beep();
        ev3.getGraphicsLCD().clear();
        LCD.clearDisplay();
        LCD.drawString("Eike ist gay", 5,5);


        RMISampleProvider sensor = ev3.createSampleProvider("S4", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");

        float[] sample = new float[1];
        try {
            sample =  sensor.fetchSample();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println(sample[0]);
        Delay.msDelay(5000);


        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            try{
                sensor.close();
            }catch (Exception e){
                System.err.println(e);
            }
        }));

/*
        new Thread(() -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }).start();



        EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(SensorPort.S4);

        SampleProvider distance = sensor.getMode("Distance");
        float[] sample = new float[distance.sampleSize()];

        distance.fetchSample(sample, 0);
        sensor.close();

        Delay.msDelay(100);

        LCD.clear();
        LCD.drawString(Float.toString(sample[0]), 0 , 5);


        Drive.drive(3);
        LCD.clear();
        LCD.drawString("turn left",0,5);
        LCD.refresh();

        Drive.turn90Degrees(true);

        LCD.clear();
        LCD.drawString("sound",0,5);
        LCD.refresh();

        Drive.stop();

        NXTSoundSensor sound = new NXTSoundSensor(SensorPort.S2);


        boolean start = false;
        while(!start){
            float[] a = new float[2];
            sound.getDBAMode().fetchSample(a,0);
            LCD.drawString(Float.toString(a[0]), 0 , 5);

            if(a[0] > 0.4f){
                start = true;
            }
        }

        Drive.driveForward();
        Delay.msDelay(3000);
        start = false;
        while(!start){
            float[] a = new float[2];
            sound.getDBAMode().fetchSample(a,0);
            LCD.drawString(Float.toString(a[0]), 0 , 5);

            if(a[0] > 0.4){
                start = true;
            }
        }
        */
    }
}
