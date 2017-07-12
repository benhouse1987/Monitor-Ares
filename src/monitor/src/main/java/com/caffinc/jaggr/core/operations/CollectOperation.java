package com.caffinc.jaggr.core.operations;


import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Collects all values into a list
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class CollectOperation implements Operation {
    private String[] field;
    private String unsplitField;

    public CollectOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        Object value = FieldValueExtractor.getValue(field, object);
        if (value == null)
            return previousAccumulatedValue;
        List<Object> accumulator = previousAccumulatedValue == null
                ? new ArrayList<>()
                : (List<Object>) previousAccumulatedValue;
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public Object result(Object accumulatedValue) {
        return accumulatedValue;
    }
}
