package components;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.SampleProvider;

import java.io.Closeable;

public class MyGyroSensor {
    EV3GyroSensor gyroSensor;
    private MyRMISampleProvider myRMISampleProvider;
    private SampleProvider sampleProvider;
    private float[] floatArr;


    public MyGyroSensor(Port p) {
        gyroSensor = new EV3GyroSensor(p);

        this.sampleProvider = gyroSensor.getAngleMode();
        this.floatArr = new float[1];
        myRMISampleProvider = null;
    }

    public MyGyroSensor(RemoteEV3 ev3, Port p, String modus) {

        //TODO: FIX .createSampleProvider!
        myRMISampleProvider = new MyRMISampleProvider(ev3.createSampleProvider(p.getName(),
                "lejos.hardware.sensor.EV3GyroSensor", modus));

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
        if (sampleProvider != null) {
            return myRMISampleProvider;
        } else {
            return gyroSensor;
        }
    }

}
