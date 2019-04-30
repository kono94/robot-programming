package utils;

/**
 * Helper class to normalize the given colors
 * from 0 to 1.
 */
public class Normalizer {
    private float lowerBorder ;
    private float upperBorder;
    private float min;
    private float max;
    private float a;
    private float b;

    public Normalizer(float fromMin, float fromMax, float toMin, float toMax){
        this.min = fromMin;
        this.max = fromMax;
        this.lowerBorder = toMin;
        this.upperBorder = toMax;
        calculateConstants();
    }

    private void calculateConstants(){
        a = (upperBorder - lowerBorder)/(max-min);
        b = upperBorder - a * max;
    }
    public float normalizeValue(float value){
        return a * value + b;
    }
}
