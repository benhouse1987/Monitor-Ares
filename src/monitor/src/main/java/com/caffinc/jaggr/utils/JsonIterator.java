package com.caffinc.jaggr.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Converts any Iterator into a JSON Iterator for <code>jaggr</code>
 *
 * @author Sriram
 * @since 11/28/2016
 */
public abstract class JsonIterator<T> implements Iterator<Map<String, Object>> {
    private Iterator<T> iterator;

    /**
     * Constructs an iterator wrapper of the objects in the passed <code>Iterator</code>.
     *
     * @param iterator the underlying <code>Iterator</code> to read from, not null
     * @throws IllegalArgumentException if the reader is null
     */
    public JsonIterator(Iterator<T> iterator) {
        if (iterator == null)
            throw new IllegalArgumentException("Iterator must not be null");
        this.iterator = iterator;
    }

    /**
     * Indicates whether the underlying <code>Iterator</code> has more objects
     *
     * @return {@code true} if the Iterator has more objects
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Returns the next object in the wrapped <code>Iterator</code>.
     *
     * @return the next JSON object from the input
     * @throws NoSuchElementException if there is no object to return
     */
    @Override
    public Map<String, Object> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more objects");
        }
        return toJson(iterator.next());
    }

    /**
     * Unsupported.
     *
     * @throws UnsupportedOperationException always
     */
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on JsonFileIterator");
    }


    public abstract Map<String, Object> toJson(T element);
}
