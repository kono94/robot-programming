package de.scr.ev3.components;

public interface Drivable {
    void drive(int speed, int turn);
    void rotateOnPlace(int degree, MyGyroSensor gyroSensor);
    void rotateOnPlace(int speed, int degree, MyGyroSensor gyroSensor);
    void drive(int turn);
    void setSpeed(int speed);
    int getSpeed();
}
