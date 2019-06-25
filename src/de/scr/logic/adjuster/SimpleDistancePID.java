package de.scr.logic.adjuster;

public class SimpleDistancePID implements Adjuster {
    private IntegralManager integralManager;
    private ProportionalManager proportionalManager;
    private DifferentialManager differentialManager;

    private float Kp;
    private float Ki;
    private float Kd;
    private int minAdjustment = -100;
    private int maxAdjustment = 100;
    private float desiredValue;

    public SimpleDistancePID(float desiredValue, float p, float i, float d) {
        Kp = p;
        Ki = i;
        Kd = d;
        this.desiredValue = desiredValue;
        this.integralManager = new IntegralManager();
        this.proportionalManager = new ProportionalManager();
        this.differentialManager = new DifferentialManager();
    }

    @Override
    public int calculateAdjustment(float normSensorValue) {
        float error = desiredValue - normSensorValue;
        float pValue = proportionalManager.feedAndGet(error);
        float iValue = integralManager.feedAndGet(error);
        float dValue = differentialManager.feedAndGet(error);

        int adjustment = (int) (Kp * pValue + Ki * iValue + Kd * dValue);
        // Stronger priority when moving backwards
        adjustment = adjustment > 0 ? -adjustment * 2 : -adjustment;

        if (adjustment < minAdjustment) return minAdjustment;
        else if (adjustment > maxAdjustment) return maxAdjustment;
        else return adjustment;
    }
}
