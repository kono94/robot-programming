package utils;

public class PIDController implements Adjuster{
    private final float Kp = 60f;
    private final float Ki = 0.1f;
    private final float Kd = 7000f;

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
        if(Math.abs(Kd * derivative) > 80) integral = 0;

        if (integral > maxIntegral) integral = maxIntegral;
        if (integral < minIntegral) integral = minIntegral;

        System.out.print("currentSensorValue:" + currentSensorValue + "error: " + Kp * error + " integral: " + integral + " derivative: " + Kd * derivative);

        int adjustment = (int) (Kp * error + integral + Kd * derivative);
        System.out.println("\t adjustment" + adjustment);
        if (adjustment > highLimitAdjustment) adjustment = highLimitAdjustment;
        if (adjustment < lowLimitAdjustment) adjustment = lowLimitAdjustment;

        previousError = error;
        dt = (int) (System.currentTimeMillis() - this.cycleTime);
        this.cycleTime = System.currentTimeMillis();
        //todo: needs to be inverted to match turn
        return -adjustment;
    }

/*
    public static void main(String[] args) {
        PIDController p = new PIDController(0);

        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float inp = (float)(Math.random() * -1) + 1;
            System.out.println("inp "  + inp);
            System.out.println(p.calculateAdjustment(inp));
        }
    }
    */

}
