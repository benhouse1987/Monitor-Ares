package com.caffinc.jaggr.core.operations;


import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Collects all values into a set
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class CollectSetOperation implements Operation {
    private String[] field;
    private String unsplitField;

    public CollectSetOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        Object value = FieldValueExtractor.getValue(field, object);
        if (value == null)
            return previousAccumulatedValue;
        Set<Object> accumulator = previousAccumulatedValue == null
                ? new HashSet<>()
                : (Set<Object>) previousAccumulatedValue;
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public Object result(Object accumulatedValue) {
        return accumulatedValue;
    }
}
