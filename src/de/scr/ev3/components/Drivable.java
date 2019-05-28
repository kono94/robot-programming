package de.scr.ev3.components;

public interface Drivable {
    void drive(int speed, int turn);
    void rotateOnPlace(int speed, int degree);
    void drive(int turn);
    void setSpeed(int speed);
    int getSpeed();
}
