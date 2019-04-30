package utils;

public class PIDController2 {
    IntegralManager integralManager;
    ProportionalManager proportionalManager;
    DifferentialManager differentialManager;
    private Normalizer normalizer;
    private float Kp = 1.0f;
    private float Ki = 1.0f;
    private float Kd = 1.0f;

    public PIDController2(){
        this.integralManager = new IntegralManager();
        this.proportionalManager = new ProportionalManager();
        this.differentialManager = new DifferentialManager();
        this.normalizer = new Normalizer(0, 1, -1, 1);
    }

    private float pid(float sensorInput){
        float error = normalizer.normalizeValue(sensorInput);
        float pValue = proportionalManager.feedAndGet(error);
        float iValue = integralManager.feedAndGet(error);
        float dValue = differentialManager.feedAndGet(error);

        return Kp * pValue + Ki * iValue + Kd * dValue;
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
