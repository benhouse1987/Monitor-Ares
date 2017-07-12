package com.caffinc.jaggr.core.operations;

import java.util.Map;

/**
 * Interface for an operation in the aggregation
 *
 * @author Sriram
 * @since 11/24/2016
 */
public interface Operation {
    Object aggregate(Object previousAccumulatedValue, Map<String, Object> object);

    Object result(Object accumulatedValue);
}
