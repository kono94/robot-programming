package utils;

public class IntegralManager{
    private static final int SampleRange = 10;

    private float[] yValues;
    private int index;
    private Normalizer normalizer;

    public IntegralManager(){
        yValues = new float[SampleRange];
        index = 0;
        normalizer = new Normalizer(0, SampleRange, -1, 1);
    }

    /**
     * Passes current error value and returns
     * the normalized value of the integral of
     * the last error values;
     *
     * @param y current error value
     * @return normalized value of surface from -1 to 1
     */
    public float feedAndGet(float y){
        feed(y);
        return get();
    }

    /**
     *  Calculating the integral of the last <SampleRange> values;
     *  (just adding up all values in fact)
     *
     * @return normalized value of surface from -1 to 1
     */
    public float get(){
        float sum = 0;
        for(float y : yValues){
            sum += y;
        }
        return normalizer.normalizeValue(sum);
    }

    /**
     *
     * @param y current error value
     */
    public void feed(float y){
        index = ++index % SampleRange;
        yValues[index] = y;
    }
}
