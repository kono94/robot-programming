package utils;

public class PIDController2 implements Adjuster{
    IntegralManager integralManager;
    ProportionalManager proportionalManager;
    DifferentialManager differentialManager;

    private float Kp = 1.0f;
    private float Ki = 1.0f;
    private float Kd = 1.0f;

    public PIDController2(){
        this.integralManager = new IntegralManager();
        this.proportionalManager = new ProportionalManager();
        this.differentialManager = new DifferentialManager();
    }

    @Override
    public int calculateAdjustment(float error){
        float pValue = proportionalManager.feedAndGet(error);
        float iValue = integralManager.feedAndGet(error);
        float dValue = differentialManager.feedAndGet(error);

        return (int) (Kp * pValue + Ki * iValue + Kd * dValue);
    }

    /*
    public static void main(String[] args) {
        PIDController2 p = new PIDController2();

        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float inp = (float)(Math.random() * -1) + 1;
            System.out.println("inp "  + inp);
            System.out.println(p.pid(inp));
        }
    }
    */
}
