package components;

import lejos.hardware.Sound;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.utility.Delay;

import java.rmi.RemoteException;

public class DriveRemote implements Drivable {
    private RMIRegulatedMotor left;
    private RMIRegulatedMotor right;

    public DriveRemote(RMIRegulatedMotor left, RMIRegulatedMotor right) {
        this.left = left;
        this.right = right;
    }

    public void drive(int speed, float turn) {
        try {
            System.out.println(right.getMaxSpeed());

            left.setSpeed(speed);
            right.setSpeed(speed);
            right.forward();
            left.backward();
            /*
            200 => 4000
            1 => 800000

             */
            Delay.msDelay(3875);
            System.out.println(right.getTachoCount());

            right.stop(true);

            left.stop(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void rotateOnPlace(int speed, int degree) {
        try {
            right.stop(true);
            left.stop(true);
            right.setSpeed(speed);
            left.setSpeed(speed);
            right.forward();
            left.backward();
            Delay.msDelay((772000 / speed * degree) / 360);
            left.stop(true);
            right.stop(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
