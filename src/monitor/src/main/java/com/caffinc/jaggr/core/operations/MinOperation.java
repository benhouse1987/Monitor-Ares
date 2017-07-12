package com.caffinc.jaggr.core.operations;


import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.Map;

/**
 * Performs minimum value aggregation
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class MinOperation implements Operation {
    private String[] field;
    private String unsplitField;
    private boolean isDouble = false;

    public MinOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        Object value = FieldValueExtractor.getValue(field, object);
        if (value == null)
            return previousAccumulatedValue;
        Double accumulator = previousAccumulatedValue == null
                ? Double.MAX_VALUE
                : (Double) previousAccumulatedValue;
        if (value instanceof Double) {
            isDouble = true;
            return (accumulator > (Double) value) ? value : accumulator;
        }
        if (value instanceof Float) {
            isDouble = true;
            return (accumulator > (Float) value) ? value : accumulator;
        }
        if (value instanceof Long) {
            return (accumulator > (Long) value) ? value : accumulator;
        }
        if (value instanceof Integer) {
            return (accumulator > (Integer) value) ? value : accumulator;
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
