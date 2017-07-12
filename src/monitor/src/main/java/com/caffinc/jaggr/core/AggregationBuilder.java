package com.caffinc.jaggr.core;


import com.caffinc.jaggr.core.operations.Operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder for Aggregations
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class AggregationBuilder {
    private String _id = null;
    private Map<String, Operation> operationMap = new HashMap<>();

    /**
     * Sets the field to group the aggregation by. If field is not set, all documents are grouped together.
     *
     * @param field Grouping Field
     * @return <code>this</code>
     */
    public AggregationBuilder setGroupBy(String field) {
        _id = field;
        return this;
    }

    /**
     * Adds an aggregation operation to this builder
     *
     * @param field     Field to store the result of this aggregation in
     * @param operation Operation to perform
     * @return <code>this</code>
     */
    public AggregationBuilder addOperation(String field, Operation operation) {
        operationMap.put(field, operation);
        return this;
    }

    /**
     * Gets the <code>BatchAggregation</code> object which can be used to aggregate batches of data
     *
     * @return <code>BatchAggregation</code> which can perform the grouping and operations set
     * previously on this <code>AggregationBuilder</code>
     */
    public BatchAggregation getBatchAggregation() {
        return new BatchAggregation(_id, Collections.unmodifiableMap(operationMap));
    }

    /**
     * Gets the <code>Aggregation</code> object which can be used to aggregate streams of data
     *
     * @return <code>Aggregation</code> which can perform the grouping and operations set
     * previously on this <code>AggregationBuilder</code>
     */
    public Aggregation getAggregation() {
        return new Aggregation(_id, Collections.unmodifiableMap(operationMap));
    }
}
