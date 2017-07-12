package com.caffinc.jaggr.core.operations;

import com.caffinc.jaggr.core.entities.Tuple2;
import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.Map;

/**
 * Performs averaging aggregation
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class AverageOperation implements Operation {
    private String[] field;
    private String unsplitField;

    public AverageOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        Object value = FieldValueExtractor.getValue(field, object);
        if (value == null)
            return previousAccumulatedValue;
        Tuple2<Double, Integer> accumulator = previousAccumulatedValue == null
                ? new Tuple2<>(0.0d, 0)
                : (Tuple2<Double, Integer>) previousAccumulatedValue;

        if (value instanceof Double) {
            accumulator._1 = accumulator._1 + (Double) value;
        } else if (value instanceof Float) {
            accumulator._1 = accumulator._1 + (Float) value;
        } else if (value instanceof Long) {
            accumulator._1 = accumulator._1 + (Long) value;
        } else if (value instanceof Integer) {
            accumulator._1 = accumulator._1 + (Integer) value;
        } else {
            throw new IllegalArgumentException("Field " + unsplitField + " isn't a Double, Float, Long or Integer");
        }
        accumulator._2++;
        return accumulator;
    }

    @Override
    public Object result(Object accumulatedValue) {
        Tuple2<Double, Integer> accumulator = accumulatedValue == null
                ? new Tuple2<>(0.0d, 1)
                : (Tuple2<Double, Integer>) accumulatedValue;
        return accumulator._1 / accumulator._2;
    }
}
