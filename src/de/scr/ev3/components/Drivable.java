package de.scr.ev3.components;

/**
 * Interface which originally got implemented when there was no MotorWrapper
 * class available and the "Drive" class was split into "DriveLocal" and "DriveRemote"
 * to handle "RemoteException".
 */
public interface Drivable {
    void drive(int speed, int turn);

    void rotateOnPlace(int degree, MyGyroSensor gyroSensor);

    void rotateOnPlace(int speed, int degree, MyGyroSensor gyroSensor);

    void drive(int turn);

    void drive();

    int getSpeed();

    void setSpeed(int speed);

    int getTurn();

    void setTurn(int turn);
}
