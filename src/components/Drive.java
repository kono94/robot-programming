package components;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Drive {
    public static EV3LargeRegulatedMotor leftWheel;
    public static EV3LargeRegulatedMotor rightWheel;

    static {
        leftWheel = new EV3LargeRegulatedMotor(MotorPort.B);
        rightWheel = new EV3LargeRegulatedMotor(MotorPort.C);
    }

    public void drive(int distance, float turn){
        if(distance > 0){
            leftWheel.forward();
            rightWheel.forward();
        }else{
            leftWheel.backward();
            rightWheel.backward();
        }
        Delay.msDelay(distance*1000);
    }

    public static void turn90Degrees(boolean turnLeft){
        if(turnLeft){
            leftWheel.stop();
            rightWheel.forward();
            Delay.msDelay(1000);
        }else{
            rightWheel.stop();
            leftWheel.forward();
            Delay.msDelay(1000);
        }
    }


    public static void stop(){
        rightWheel.stop();
        leftWheel.stop();
    }
    public static void driveForward(){
        rightWheel.forward();
        leftWheel.forward();
    }
}
