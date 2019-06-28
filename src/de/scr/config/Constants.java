package de.scr.config;

import de.scr.utils.RunControl;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Requires EV3-Brick connection
 */
public class Constants {
    public static final RunControl START_MODE = RunControl.LINE_EVADE;
    public static final String REMOTE_HOST = "10.0.1.1";
    public static final Port MOTOR_PORT_LEFT = MotorPort.A;
    public static final Port MOTOR_PORT_RIGHT = MotorPort.D;
    public static final Port COLOR_SENSOR_PORT = SensorPort.S2;
    public static final Port COLOR_SENSOR_2_PORT = SensorPort.S4;
    public static final Port GYRO_SENSOR_PORT = SensorPort.S1;
    public static final Port DISTANCE_SENSOR_PORT = SensorPort.S3;
    public static final int DEFAULT_SPEED = 15;
}