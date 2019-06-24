package de.scr.ev3.components;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.remote.ev3.RMIRegulatedMotor;

import java.rmi.RemoteException;

public class MotorWrapper {
    private EV3LargeRegulatedMotor motor;
    private RMIRegulatedMotor rmiMotor;

    public MotorWrapper(EV3LargeRegulatedMotor m) {
        motor = m;
        rmiMotor = null;
    }

    public MotorWrapper(RMIRegulatedMotor m) {
        rmiMotor = m;
        motor = null;
    }

    public float getMaxSpeed() {
        if (motor == null) {
            try {
                return rmiMotor.getMaxSpeed();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            return motor.getMaxSpeed();
        }
        return -1;
    }


    public float getSpeed() {
        if (motor == null) {
            try {
                return rmiMotor.getSpeed();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            return motor.getSpeed();
        }
        return -1;
    }

    public void setSpeed(int i) {
        if (motor == null) {
            try {
                rmiMotor.setSpeed(i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            motor.setSpeed(i);
        }
    }

    public void forward() {
        if (motor == null) {
            try {
                rmiMotor.forward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            motor.forward();
        }
    }

    public void backward() {
        if (motor == null) {
            try {
                rmiMotor.backward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            motor.backward();
        }
    }

    public void stop(boolean b) {
        if (motor == null) {
            try {
                rmiMotor.stop(b);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            motor.stop(b);
        }
    }
}
