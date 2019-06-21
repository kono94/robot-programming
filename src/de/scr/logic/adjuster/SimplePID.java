package de.scr.logic.adjuster;

public class SimplePID implements Adjuster{
    IntegralManager integralManager;
    ProportionalManager proportionalManager;
    DifferentialManager differentialManager;

    /*
    values for follow line remote:
    P = 80
    I = 50
    D = 40
     */


    private float Kp = 80;
    private float Ki = 50;
    private float Kd = 40;
    private int minAdjustment = -100;
    private int maxAdjustment = 100;
    private float desiredValue;

    public SimplePID(float desiredValue, float p, float i, float d){
        Kp = p;
        Ki = i;
        Kd = d;
        this.desiredValue = desiredValue;
        this.integralManager = new IntegralManager();
        this.proportionalManager = new ProportionalManager();
        this.differentialManager = new DifferentialManager();
    }

    public int getMinAdjustment() {
        return minAdjustment;
    }

    public void setMinAdjustment(int minAdjustment) {
        this.minAdjustment = minAdjustment;
    }

    public int getMaxAdjustment() {
        return maxAdjustment;
    }

    public void setMaxAdjustment(int maxAdjustment) {
        this.maxAdjustment = maxAdjustment;
    }

    @Override
    public int calculateAdjustment(float normSensorValue){
        System.out.println(normSensorValue);
        float error = desiredValue - normSensorValue;
        float pValue = proportionalManager.feedAndGet(error);
        float iValue = integralManager.feedAndGet(error);
        float dValue = differentialManager.feedAndGet(error);

        //System.out.println("P: " + Kp * pValue + " I: " + Ki * iValue  +  " D:" + Kd * dValue  + "\t adjustment: " + (Kp * pValue + Ki * iValue + Kd * dValue));
        int adjustment = (int) (Kp * pValue + Ki * iValue + Kd * dValue);
        adjustment = -adjustment;
        if(adjustment < minAdjustment) return minAdjustment;
        else if(adjustment > maxAdjustment) return maxAdjustment;
        else return adjustment;
    }
}
