package main;


import programs.TestProgram;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        TestProgram t = new TestProgram();
        try {
            t.start(false);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }

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
