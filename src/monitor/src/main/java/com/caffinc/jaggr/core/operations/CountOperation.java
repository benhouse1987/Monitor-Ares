package com.caffinc.jaggr.core.operations;

import java.util.Map;

/**
 * Performs count aggregation
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class CountOperation implements Operation {
    private Integer counterValue;

    public CountOperation(Integer counterValue) {
        this.counterValue = counterValue;
    }

    public CountOperation() {
        this(1);
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        return (previousAccumulatedValue == null ? 0 : (Integer) previousAccumulatedValue) + counterValue;
    }

    @Override
    public Object result(Object accumulatedValue) {
        return accumulatedValue;
    }
}
