package de.scr.ev3.components;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.SampleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

public class MyGyroSensor {
    private static Logger logger = LoggerFactory.getLogger(MyGyroSensor.class);
    private EV3GyroSensor gyroSensor;
    private MyRMISampleProvider myRMISampleProvider;
    private SampleProvider sampleProvider;
    private float[] floatArr;


    public MyGyroSensor(Port p) {
        logger.info("Creating local GyroSensor");
        gyroSensor = new EV3GyroSensor(p);

        this.sampleProvider = gyroSensor.getAngleMode();
        this.floatArr = new float[1];
        myRMISampleProvider = null;
    }

    public MyGyroSensor(RemoteEV3 ev3, Port p, String modeName) {
        logger.info("Creating RMI-GyroSensor");

        myRMISampleProvider = new MyRMISampleProvider(ev3.createSampleProvider(
                p.getName(), "lejos.hardware.sensor.EV3GyroSensor", modeName));
        
        this.floatArr = new float[1];
        sampleProvider = null;
    }


    public float getAngle() {
        if (sampleProvider != null) {
            sampleProvider.fetchSample(floatArr, 0);
            return floatArr[0];
        } else {
            return myRMISampleProvider.fetchSample()[0];
        }
    }

    public Closeable getCloseable() {
        return sampleProvider != null ? gyroSensor : myRMISampleProvider;
    }

}
