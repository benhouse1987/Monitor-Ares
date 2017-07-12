package com.caffinc.jaggr.core.operations;


import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.Map;

/**
 * Performs summing aggregation
 *
 * @author Sriram
 * @since 11/24/2016
 */
public class SumOperation implements Operation {
    private String[] field;
    private String unsplitField;
    private boolean isDouble = false;

    public SumOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        Object value = FieldValueExtractor.getValue(field, object);
        if (value == null)
            return previousAccumulatedValue;
        Double accumulator = previousAccumulatedValue == null ? 0.0d : (Double) previousAccumulatedValue;
        if (value instanceof Double) {
            isDouble = true;
            return accumulator + (Double) value;
        }
        if (value instanceof Float) {
            isDouble = true;
            return accumulator + (Float) value;
        }
        if (value instanceof Long) {
            return accumulator + (Long) value;
        }
        if (value instanceof Integer) {
            return accumulator + (Integer) value;
        }
        throw new IllegalArgumentException("Field " + unsplitField + " isn't a Double, Float, Long or Integer");
    }

    @Override
    public Object result(Object accumulatedValue) {
        if (accumulatedValue != null)
            return isDouble ? accumulatedValue : ((Double) accumulatedValue).longValue();
        else
            return null;
    }
}
