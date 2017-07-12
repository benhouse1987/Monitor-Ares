package com.caffinc.jaggr.core.operations;


import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.Map;

/**
 * Gets the first non-null object in the stream
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class FirstObjectOperation implements Operation {
    private String[] field;
    private String unsplitField;

    public FirstObjectOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        if (previousAccumulatedValue == null)
            return FieldValueExtractor.getValue(field, object);
        else
            return previousAccumulatedValue;
    }

    @Override
    public Object result(Object accumulatedValue) {
        return accumulatedValue;
    }
}
