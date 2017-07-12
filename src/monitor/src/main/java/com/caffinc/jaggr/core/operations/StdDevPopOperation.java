package com.caffinc.jaggr.core.operations;

import com.caffinc.jaggr.core.entities.Tuple3;
import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.Map;

/**
 * Computes Standard Deviation of the Population using Welford's method
 *
 * @author Sriram
 * @since 11/29/2016
 */
public class StdDevPopOperation implements Operation {
    private String[] field;
    private String unsplitField;

    public StdDevPopOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        Object value = FieldValueExtractor.getValue(field, object);
        if (value == null)
            return previousAccumulatedValue;
        Tuple3<Double, Double, Integer> accumulator = previousAccumulatedValue == null
                ? new Tuple3<>(0.0d, 0.0d, 0)
                : (Tuple3<Double, Double, Integer>) previousAccumulatedValue;

        Double parsedValue = 0d;
        if (value instanceof Double) {
            parsedValue = (Double) value;
        } else if (value instanceof Float) {
            parsedValue += (Float) value;
        } else if (value instanceof Long) {
            parsedValue += (Long) value;
        } else if (value instanceof Integer) {
            parsedValue += (Integer) value;
        } else {
            throw new IllegalArgumentException("Field " + unsplitField + " isn't a Double, Float, Long or Integer");
        }
        accumulator._3++;
        double tmpM = accumulator._1;
        accumulator._1 += (parsedValue - tmpM) / accumulator._3;
        accumulator._2 += (parsedValue - tmpM) * (parsedValue - accumulator._1);
        return accumulator;
    }

    @Override
    public Object result(Object accumulatedValue) {
        Tuple3<Double, Double, Integer> accumulator = accumulatedValue == null
                ? null
                : (Tuple3<Double, Double, Integer>) accumulatedValue;
        if (accumulator == null || accumulator._3 == 1 || accumulator._2 < 0)
            return null;
        else
            return Math.sqrt(accumulator._2 / (accumulator._3 - 1));
    }
}
