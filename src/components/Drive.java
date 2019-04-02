package components;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Drive implements Drivable{
    private EV3LargeRegulatedMotor left;
    private EV3LargeRegulatedMotor right;

    public Drive(EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right){
        this.left = left;
        this.right = right;
    }

    public void drive(int speed, int turn){
        int maxSpeed = (int) right.getMaxSpeed();
        int customSpeed = (maxSpeed * speed)/100;
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

        if(turn > 0 && turn <= 50){
            right.setSpeed(customSpeed);
            right.forward();
            left.setSpeed((int) (customSpeed * ((100 - turn)/ (double)100)));
            left.forward();
        }else if(turn > 50){
            right.setSpeed(customSpeed);
            right.forward();
            left.setSpeed((int) (customSpeed * ((turn)/ (double)100)));
            left.backward();
        }else if(turn >= -50){
            left.setSpeed(customSpeed);
            left.forward();
            right.setSpeed((int) (customSpeed * (100 -(-turn))/(double)100));
            right.forward();
        }else{
            left.setSpeed(customSpeed);
            left.forward();
            right.setSpeed((int) (customSpeed * (-turn)/(double)100));
            right.backward();
        }
    }

    @Override
    public void rotateOnPlace(int speed, int degree) {

    }
}
