package de.scr.logic.adjuster;

public class PIDController implements Adjuster{
    private float Kp = 65f;
    private float Ki = 0.1f;
    private float Kd = 8000f;

    private float setPoint;
    private float previousError = 0f;
    private float integral = 0f;
    private float derivative = 0f;
    private long cycleTime = 0;

    private int dt = 1;
    private int highLimitAdjustment = 100;
    private int lowLimitAdjustment = -100;
    private int maxIntegral = 50;
    private int minIntegral = -50;
    private int msDelay = 100;

    public PIDController(float setPoint) {
        this.setPoint = setPoint;
    }


    @Override
    public int calculateAdjustment(float currentSensorValue) {
        if (this.cycleTime == 0) {
            this.cycleTime = System.currentTimeMillis();
            return 0;
        }

        // Proportional
        float error = setPoint - currentSensorValue;
        // Integral
        integral += Ki * error * dt;
        // Differential
        derivative =  (error - previousError) / dt;

        if (integral > maxIntegral) integral = maxIntegral;
        if (integral < minIntegral) integral = minIntegral;

        int adjustment = (int) (Kp * error + integral + Kd * derivative);
        if (adjustment > highLimitAdjustment) adjustment = highLimitAdjustment;
        if (adjustment < lowLimitAdjustment) adjustment = lowLimitAdjustment;

        previousError = error;
        dt = (int) (System.currentTimeMillis() - this.cycleTime);
        this.cycleTime = System.currentTimeMillis();
        return adjustment;
    }

    public void setSetPoint(float setPoint) {
        this.setPoint = setPoint;
    }

    public void setKp(float kp) {
        Kp = kp;
    }

    public void setKi(float ki) {
        Ki = ki;
    }

    public void setKd(float kd) {
        Kd = kd;
    }

    public void setMaxIntegral(int maxIntegral) {
        this.maxIntegral = maxIntegral;
    }

    public void setMinIntegral(int minIntegral) {
        this.minIntegral = minIntegral;
    }
}
