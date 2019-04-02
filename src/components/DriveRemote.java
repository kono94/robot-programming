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

    /**
     *
     * @param speed from -100 to 100 in percent of max speed, negative equals backwards
     * @param turn from -100 to 100 relation between left (-) and right (+) wheel; 100 only the right wheel is
     *             rotation in speed %; 0 equals straight forward
     */
    public void drive(int speed, int turn) {
        try {
            int maxSpeed = (int) (right.getMaxSpeed()*0.5);
            int customSpeed = (maxSpeed * speed)/100;
            /*
                 300  turn 0 = left 300 right 300
                 300  turn 50 = left 150 right 300
                 300 turn 100 = left 0 right 300

                 300 turn -50 = left 300 right 150
                 300 turn 100 = left 300 right 0
             */

            if(turn > 0){
                right.setSpeed(customSpeed);
                left.setSpeed(customSpeed * (turn/100));
            }else{
                left.setSpeed(customSpeed);
                right.setSpeed(customSpeed * (-turn/100));
            }

            System.out.println("Left-Speed: " + left.getSpeed());
            System.out.println("Right-Speed: " + right.getSpeed());

            if(speed > 0){
                right.forward();
                left.forward();
            }else if(speed < 0 ){
                right.backward();
                left.backward();
            }else{
                right.stop(true);
                left.stop(true);
            }

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
