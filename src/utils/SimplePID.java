package utils;

public class SimplePID implements Adjuster{
    IntegralManager integralManager;
    ProportionalManager proportionalManager;
    DifferentialManager differentialManager;

    private float Kp = 60f;
    private float Ki = 20f;
    private float Kd = 30f;

    public SimplePID(){
        this.integralManager = new IntegralManager();
        this.proportionalManager = new ProportionalManager();
        this.differentialManager = new DifferentialManager();
    }

    @Override
    public int calculateAdjustment(float error){
        float pValue = proportionalManager.feedAndGet(error);
        float iValue = integralManager.feedAndGet(error);
        float dValue = differentialManager.feedAndGet(error);
        System.out.println("P: " + Kp * pValue + " I: " + Ki * iValue  +  " D:" + Kd * dValue  + "\t adjustment: " + (Kp * pValue + Ki * iValue + Kd * dValue));
        return (int) (Kp * pValue + Ki * iValue + Kd * dValue);
    }
}
