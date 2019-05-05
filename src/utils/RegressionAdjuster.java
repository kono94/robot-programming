package utils;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import java.util.ArrayList;
import java.util.List;

public class RegressionAdjuster implements Adjuster {

    private float min;
    private float max;
    private double[] weights;

    public RegressionAdjuster(float min, float max){
        this.min = min;
        this.max = max;
        calculateWeights();
    }

    @Override
    public int calculateAdjustment(float currentSensorValue) {
        return (int) (weights[2] * currentSensorValue * currentSensorValue + weights[1] * currentSensorValue + weights[0]);
    }

    // Uses polynomial-regression to create a function (the weights) 2-degree
    // a * x^2 + b * x + c
    private void calculateWeights(){
        PolynomialCurveFitter pcf = PolynomialCurveFitter.create(2);
        List<WeightedObservedPoint> p = new ArrayList<>();

        double middle = (min + max) / 2;
        double diff = max -min;
        double ten = diff * 0.1;

        p.add(new WeightedObservedPoint(80, min, -100));
        p.add(new WeightedObservedPoint(80, max, 100));
        p.add(new WeightedObservedPoint(50, middle, 0));
        p.add(new WeightedObservedPoint(50, middle + ten, 0));
        p.add(new WeightedObservedPoint(50, middle - ten, 0));
        p.add(new WeightedObservedPoint(55, max - ten, 30));

        weights = pcf.fit(p);
    }

}
