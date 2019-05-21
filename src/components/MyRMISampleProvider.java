package components;

import lejos.remote.ev3.RMISampleProvider;

import java.io.Closeable;
import java.rmi.RemoteException;

public class MyRMISampleProvider implements Closeable {
    private RMISampleProvider rmiSampleProvider;

    public MyRMISampleProvider(RMISampleProvider rmi){
        this.rmiSampleProvider = rmi;
    }

    public float[] fetchSample() {
        try {
            return rmiSampleProvider.fetchSample();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        try {
            rmiSampleProvider.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
