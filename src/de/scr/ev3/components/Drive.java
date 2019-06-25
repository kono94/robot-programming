package de.scr.ev3.components;

import de.scr.config.Constants;
import lejos.utility.Delay;

public class Drive implements Drivable {
    private static final int TURN_THRESHOLD = 90;
    private MotorWrapper left;
    private MotorWrapper right;
    private int speed;

    public Drive(MotorWrapper left, MotorWrapper right) {
        this.left = left;
        this.right = right;
        speed = Constants.DEFAULT_SPEED;
    }

    /**
     * @param speed from -100 to 100 in percent of max speed, negative equals backwards
     * @param turn  from -100 to 100 relation between left (-) and right (+) wheel; 100 only the right wheel is
     *              rotation in speed %; 0 equals straight forward
     */
    public void drive(int speed, int turn) {
        this.speed = speed;
        int customSpeed = getPercentSpeed(speed);
        if (turn >= 0 && turn < TURN_THRESHOLD) {
            if (speed > 0) {
                right.setSpeed((int) (customSpeed * ((100 - turn) / (double) 100)));
                left.setSpeed(customSpeed);
                right.forward();
                left.forward();
            } else {
                left.setSpeed(customSpeed);
                right.setSpeed((int) (customSpeed * ((100 - turn) / (double) 100)));
                right.backward();
                left.backward();
            }
        } else if (turn > TURN_THRESHOLD) {
            if (speed > 0) {
                right.setSpeed((int) (customSpeed * ((turn) / (double) 100)));
                left.setSpeed(customSpeed);
                right.backward();
                left.forward();
            } else {
                right.setSpeed((int) (customSpeed * ((turn) / (double) 100)));
                left.setSpeed(customSpeed);
                right.forward();
                left.backward();
            }
        } else if (turn > -TURN_THRESHOLD) {
            if (speed > 0) {
                left.setSpeed((int) (customSpeed * (100 - (-turn)) / (double) 100));
                right.setSpeed(customSpeed);
                right.forward();
                left.forward();
            } else {
                right.setSpeed(customSpeed);
                left.setSpeed((int) (customSpeed * (100 - (-turn)) / (double) 100));
                right.backward();
                left.backward();
            }
        } else {
            if (speed > 0) {
                left.setSpeed((int) (customSpeed * (-turn) / (double) 100));
                right.setSpeed(customSpeed);
                left.backward();
                right.forward();
            } else {
                left.setSpeed((int) (customSpeed * (-turn) / (double) 100));
                right.setSpeed(customSpeed);
                left.forward();
                right.backward();
            }

        }
    }

    public void drive(int turn) {
        this.drive(speed, turn);
    }

    public void rotateOnPlace(int degree, MyGyroSensor gyroSensor) {
        rotateOnPlace(this.speed, degree, gyroSensor);
    }

    public void rotateOnPlace(int speed, int degree, MyGyroSensor gyroSensor) {
        int customSpeed = getPercentSpeed(speed);
        right.stop(true);
        left.stop(true);
        float startAngle = gyroSensor.getAngle();
        float currentAngle = startAngle;

        if (degree > 0) { // rotate left
            right.setSpeed(customSpeed);
            left.setSpeed(customSpeed);
            right.forward();
            left.backward();
            while (currentAngle < (startAngle + degree % 360)) {
                Delay.msDelay(50);
                currentAngle = gyroSensor.getAngle();
            }
        } else { // rotate right
            right.setSpeed(customSpeed);
            left.setSpeed(customSpeed);
            right.backward();
            left.forward();
            while (currentAngle > (startAngle + degree % 360)) {
                Delay.msDelay(50);
                currentAngle = gyroSensor.getAngle();
            }
        }
        left.stop(true);
        right.stop(true);
    }

    private int getPercentSpeed(int percent) {
        return ((int) right.getMaxSpeed() * percent) / 100;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
