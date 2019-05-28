package de.scr.ev3.components;

import de.scr.config.Constants;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.utility.Delay;

import java.rmi.RemoteException;

public class DriveRemote implements Drivable {
    private RMIRegulatedMotor left;
    private RMIRegulatedMotor right;
    private int speed;

    public DriveRemote(RMIRegulatedMotor left, RMIRegulatedMotor right) {
        this.left = left;
        this.right = right;
        this.speed = Constants.DEFAULT_SPEED;
    }

    /**
     *
     * @param speed from -100 to 100 in percent of max speed, negative equals backwards
     * @param turn from -100 to 100 relation between left (-) and right (+) wheel; 100 only the right wheel is
     *             rotation in speed %; 0 equals straight forward
     */
    public void drive(int speed, int turn) {
        try {
            int maxSpeed = (int) right.getMaxSpeed();
            int customSpeed = (maxSpeed * Math.abs(speed))/100;
            /*
                 300  turn 0 = left 300 right 300
                 300  turn 50 = left 150 right 300
                 300 turn 100 = left 0 right 300

                 300 turn -50 = left 300 right 150
                 300 turn -100 = left 300 right 0

                 // right side always
                 0.01 => -50 turn
                 0.14 => 100 turn
                 0.06 => 0 turn

                 0.01     0.06        0.14
                 -50       0          100

                            I------------------I
                0.16(blue)       0.06(black)

               0.5(grey-table)              0.2-0.3 (half-half)

                 - => drive left
                 + => drive right
             */

            if(turn >= 0 && turn < 80){
                if(speed > 0){
                    right.setSpeed(customSpeed);
                    left.setSpeed((int) (customSpeed * ((100 - turn) / (double) 100)));
                    right.forward();
                    left.forward();
                }else{
                    left.setSpeed(customSpeed);
                    right.setSpeed((int) (customSpeed * ((100 - turn) / (double) 100)));
                    right.backward();
                    left.backward();
                }
            }else if(turn > 80){
                right.setSpeed(customSpeed);
                left.setSpeed((int) (customSpeed * ((turn)/ (double)100)));
                right.forward();
                left.backward();
            }else if(turn > -80){
                if(speed > 0){
                    left.setSpeed(customSpeed);
                    right.setSpeed((int) (customSpeed * (100 - (-turn)) / (double) 100));
                    right.forward();
                    left.forward();
                }else{
                    right.setSpeed(customSpeed);
                    left.setSpeed((int) (customSpeed * (100 - (-turn)) / (double) 100));
                    right.backward();
                    left.backward();
                }
            }else{
                left.setSpeed(customSpeed);
                right.setSpeed((int) (customSpeed * (-turn)/(double)100));
                left.forward();
                right.backward();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drive(int turn){
        this.drive(speed, turn);
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
