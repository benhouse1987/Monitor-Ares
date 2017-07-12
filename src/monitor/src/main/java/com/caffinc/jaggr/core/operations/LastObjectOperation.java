package com.caffinc.jaggr.core.operations;


import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.Map;

/**
 * Gets the last non-null object in the stream
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class LastObjectOperation implements Operation {
    private String[] field;
    private String unsplitField;

    public LastObjectOperation(String field) {
        this.unsplitField = field;
        this.field = field.split("\\.");
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        Object value = FieldValueExtractor.getValue(field, object);
        if (value != null)
            return value;
        else
            return previousAccumulatedValue;
    }

    @Override
    public Object result(Object accumulatedValue) {
        return accumulatedValue;
    }
}
