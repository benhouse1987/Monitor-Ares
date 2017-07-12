package com.caffinc.jaggr.core;

import com.caffinc.jaggr.core.operations.Operation;
import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.*;

/**
 * Aggregates list or iterators of JSON objects based on aggregation operations
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class Aggregation {
    private String _id;
    private String[] idSplit;
    private Map<String, Operation> operationMap;

    Aggregation(String _id, Map<String, Operation> operationMap) {
        this._id = _id;
        this.idSplit = _id != null ? _id.split("\\.") : null;
        this.operationMap = operationMap;
    }

    /**
     * Aggregates over an Iterator of JSON Objects
     *
     * @param objectIterator JSON Object iterator
     * @return aggregation result
     */
    public List<Map<String, Object>> aggregate(final Iterator<Map<String, Object>> objectIterator) {
        return aggregate(new Iterable<Map<String, Object>>() {
            @Override
            public Iterator<Map<String, Object>> iterator() {
                return objectIterator;
            }
        });
    }

    /**
     * Aggregates over an iterable list of JSON Objects
     *
     * @param objectList Iterable list of JSON Objects
     * @return aggregation result
     */
    public List<Map<String, Object>> aggregate(final Iterable<Map<String, Object>> objectList) {
        Map<String, Map<String, Object>> workspace = new HashMap<>();
        if (objectList != null) {
            for (Map<String, Object> object : objectList) {
                aggregate(object, workspace);
            }
        }
        return computeResults(workspace);
    }

    public List<Map<String, Object>> aggregateCombined(final Iterable<Map<String, Object>> objectList) {
        Map<String, Map<String, Object>> workspace = new HashMap<>();
        if (objectList != null) {
            for (Map<String, Object> object : objectList) {
                aggregateCombined(object, workspace);
            }
        }
        return computeResults(workspace);
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

    private void aggregateCombined(Map<String, Object> object, Map<String, Map<String, Object>> workspace) {
        // Identify the ID of the document
        String id = "0";
        if (_id != null) {
            id = String.valueOf(FieldValueExtractor.getCombinedValue(_id, object));
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
     * Computes final results from the workspace
     *
     * @param workspace Workspace holding intermediate results
     * @return Final aggregation result
     */
    private List<Map<String, Object>> computeResults(Map<String, Map<String, Object>> workspace) {
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
}
