package de.scr.ev3.components;

import lejos.remote.ev3.RMISampleProvider;

import java.io.Closeable;
import java.rmi.RemoteException;

/**
 * Wrapper class for all RMISampleProvider.
 * Mainly implemented to add the "Closable" interface to the "RMISampleProvider"-class
 * so the shutdown-hook is able to close those as well.
 */
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
