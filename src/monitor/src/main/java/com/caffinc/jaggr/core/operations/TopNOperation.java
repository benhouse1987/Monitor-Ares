package com.caffinc.jaggr.core.operations;

import com.caffinc.jaggr.core.utils.FieldValueExtractor;

import java.util.*;

/**
 * Computes the Top N objects in the data
 *
 * @author Sriram
 * @since 11/30/2016
 */
public class TopNOperation<T> implements Operation {
    private String[] field;
    private String unsplitField;
    private int n;
    private Comparator<T> comparator;

    public TopNOperation(String field, int n, Comparator<T> comparator) {
        this.unsplitField = field;
        this.field = field.split("\\.");
        this.n = n;
        this.comparator = comparator;
    }

    @Override
    public Object aggregate(Object previousAccumulatedValue, Map<String, Object> object) {
        T value = (T) FieldValueExtractor.getValue(field, object);
        if (value == null)
            return null;
        PriorityQueue<T> priorityQueue = previousAccumulatedValue == null ? new PriorityQueue<>(n, comparator)
                : (PriorityQueue<T>) previousAccumulatedValue;
        priorityQueue.add(value);
        if (priorityQueue.size() > n)
            priorityQueue.poll();
        return priorityQueue;
    }

    @Override
    public Object result(Object accumulatedValue) {
        if (accumulatedValue == null)
            return null;
        else
            return new ArrayList<>((Collection<T>) accumulatedValue);
    }
}
