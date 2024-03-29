package de.scr.ev3.components;

import de.scr.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is essentially written as a monitor
 * to control the movement of the wheels.
 */
public class Drive implements Drivable, Monitor {
    private static Logger logger = LoggerFactory.getLogger(Drive.class);
    private static final int TURN_THRESHOLD = 90;
    private MotorWrapper left;
    private MotorWrapper right;
    private int speed;
    private int turn;

    /**
     * @param left  The left wheel from the POV of the brick
     * @param right The right wheel from the POV of the brick
     */
    public Drive(MotorWrapper left, MotorWrapper right) {
        this.left = left;
        this.right = right;
        speed = Constants.DEFAULT_SPEED;
    }

    /**
     * @param speed From -100 to 100: Percent of maximum speed. Negative indicates backwards driving
     * @param turn  From -100 to 100: Relation between left (-) and right (+) wheel; 0 means driving straight
     *              forward; Moving apart from 0 means that one wheel is getting throttled up to a certain
     *              TURN_THRESHOLD. After that, the brick is rotating on place, meaning one wheel
     *              is driving forward and the other one is driving backwards.
     */
    public synchronized void drive(int speed, int turn) {
        this.speed = speed;
        this.turn = turn;

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

    public synchronized void drive(int turn) {
        this.drive(speed, turn);
    }

    public synchronized void drive() {
        this.drive(speed, turn);
    }

    /**
     * Allows the brick to rotate up to a certain angle. Instead of just moving the wheels
     * for a pre-defined amount of time, this method uses an gyro sensor to identify
     * the current and desired angle.
     *
     * @param speed      From -100 to 100: Percent of maximum speed. Negative indicates backwards driving
     * @param degree     Amount of degrees the brick should rotate
     * @param gyroSensor Gyro sensor used to determine current angle while rotation on place
     */
    public synchronized void rotateOnPlace(int speed, int degree, MyGyroSensor gyroSensor) {
        int customSpeed = getPercentSpeed(speed);
        right.stop(true);
        left.stop(true);
        float startAngle = gyroSensor.getAngle();
        float currentAngle = startAngle;

        logger.debug("setting speed to : {}", customSpeed);
        right.setSpeed(customSpeed);
        left.setSpeed(customSpeed);

        if (degree > 0) { // rotate left
            right.forward();
            left.backward();
            while (currentAngle < (startAngle + degree % 360)) {
                currentAngle = gyroSensor.getAngle();
            }

        } else { // rotate right
            right.backward();
            left.forward();
            while (currentAngle > (startAngle + degree % 360)) {
                currentAngle = gyroSensor.getAngle();
            }

        }

        left.stop(true);
        right.stop(true);
    }

    public synchronized void rotateOnPlace(int degree, MyGyroSensor gyroSensor) {
        rotateOnPlace(this.speed, degree, gyroSensor);
    }

    private synchronized int getPercentSpeed(int percent) {
        return ((int) right.getMaxSpeed() * percent) / 100;
    }

    public synchronized int getSpeed() {
        return speed;
    }

    public synchronized void setSpeed(int speed) {
        this.speed = speed;
    }

    public synchronized int getTurn() {
        return turn;
    }

    public synchronized void setTurn(int turn) {
        this.turn = turn;
    }

}
