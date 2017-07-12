package com.caffinc.jaggr.core;

import com.caffinc.jaggr.core.operations.Operation;
import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.*;

/**
 * Aggregates batches of JSON objects based on aggregation operations
 *
 * @author Sriram
 * @since 11/29/2016
 */
public class BatchAggregation {
    private String _id;
    private String[] idSplit;
    private Map<String, Operation> operationMap;
    private Map<String, Map<String, Object>> workspace;

    /**
     * Constructs a new BatchAggregation for the given grouping <code>_id</code> field and <code>operationMap</code>
     *
     * @param _id          Field to group data on
     * @param operationMap Aggregation operations to perform
     */
    BatchAggregation(String _id, Map<String, Operation> operationMap) {
        this._id = _id;
        this.idSplit = _id != null ? _id.split("\\.") : null;
        this.operationMap = operationMap;
        this.workspace = new HashMap<>();
    }

    /**
     * Aggregates over a batch of Iterator of JSON Objects
     *
     * @param objectIterator JSON Object iterator
     * @return <code>this</code>
     */
    public BatchAggregation aggregateBatch(final Iterator<Map<String, Object>> objectIterator) {
        aggregateBatch(new Iterable<Map<String, Object>>() {
            @Override
            public Iterator<Map<String, Object>> iterator() {
                return objectIterator;
            }
        });
        return this;
    }

    /**
     * Aggregates over a batch of iterable list of JSON Objects
     *
     * @param objectList Iterable list of JSON Objects
     * @return <code>this</code>
     */
    public BatchAggregation aggregateBatch(final Iterable<Map<String, Object>> objectList) {
        if (objectList != null) {
            for (Map<String, Object> object : objectList) {
                aggregate(object, workspace);
            }
        }
        return this;
    }

    /**
     * Aggregates a single object into the workspace
     *
     * @param object    Object to perform aggregations on
     * @param workspace Workspace to hold temporary aggregation results in
     */
    private void aggregate(Map<String, Object> object, Map<String, Map<String, Object>> workspace) {
        // Identify the ID of the document
        String id = "0";
        if (_id != null) {
            id = String.valueOf(FieldValueExtractor.getValue(idSplit, object));
        }
        if (!workspace.containsKey(id)) {
            Map<String, Object> groupWorkspace = new HashMap<>();
            groupWorkspace.put("_id", id);
            workspace.put(id, groupWorkspace);
        }
        // Get the workspace for the given ID
        Map<String, Object> groupWorkspace = workspace.get(id);
        for (Map.Entry<String, Operation> operationEntry : operationMap.entrySet()) {
            // Get the key in the workspace
            String field = operationEntry.getKey();
            // Get the operation
            Operation operation = operationEntry.getValue();
            // Get the accumulated value in the workspace
            Object t0 = groupWorkspace.get(field);
            // Get the new value after performing the operation
            Object t1 = operation.aggregate(t0, object);
            // Write the result back in the workspace
            groupWorkspace.put(field, t1);
        }
    }

    /**
     * Gets the result of the aggregation in the current state
     * <p>
     * <code>Note:</code>
     * This process is resource intensive as it creates a copy of the partial result and finalizes computations
     * However this copy is a shallow copy. Further computations that operate on non-primitive results, such as
     * <code>CollectOperation</code> and <code>CollectSetOperation</code> will result in intermediate results that
     * change as more batches are computed. Handle with care. I should just remove this method :|
     * <p>
     * Use <code>getFinalResult()</code> if you require only the final result of all batches
     *
     * @return Intermediate result of computations on data processed so far
     */
    public List<Map<String, Object>> getItermediateResult() {
        return computeIntermediateResults(workspace);
    }

    /**
     * Gets the final result of the aggregation
     * <p>
     * <code>Note:</code>
     * This process finalizes the computation on the data passed to this system so far, and clears out the
     * intermediate data. Additional batches of aggregations will be treated as entirely new data.
     *
     * @return Final result of computations on data processed
     */
    public List<Map<String, Object>> getFinalResult() {
        // Compute the results
        List<Map<String, Object>> result = computeFinalResults(workspace);
        // Clear the workspace
        workspace = new HashMap<>();
        return result;
    }

    /**
     * Computes final results from the workspace
     *
     * @param workspace Workspace holding intermediate results
     * @return Final aggregation result
     */
    private List<Map<String, Object>> computeFinalResults(Map<String, Map<String, Object>> workspace) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        // Loop through all the values in the workspace map
        for (Map<String, Object> groupWorkspace : workspace.values()) {
            // Perform final result computation
            for (Map.Entry<String, Operation> operationEntry : operationMap.entrySet()) {
                String field = operationEntry.getKey();
                Operation operation = operationEntry.getValue();
                groupWorkspace.put(field, operation.result(groupWorkspace.get(field)));
            }
            resultList.add(groupWorkspace);
        }
        return resultList;
    }

    /**
     * Computes intermediate results from the workspace
     *
     * @param workspace Workspace holding intermediate results
     * @return Intermediate aggregation result
     */
    private List<Map<String, Object>> computeIntermediateResults(Map<String, Map<String, Object>> workspace) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        // Loop through all the values in the workspace map
        for (Map<String, Object> groupWorkspace : workspace.values()) {
            Map<String, Object> intermediateResult = new HashMap<>(groupWorkspace);
            // Perform result computation and insert into intermediate result
            for (Map.Entry<String, Operation> operationEntry : operationMap.entrySet()) {
                String field = operationEntry.getKey();
                Operation operation = operationEntry.getValue();
                intermediateResult.put(field, operation.result(groupWorkspace.get(field)));
            }
            resultList.add(intermediateResult);
        }
        return resultList;
    }
}
