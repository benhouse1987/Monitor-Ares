package com.caffinc.jaggr.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Iterates a JSON file
 *
 * @author Sriram
 * @since 11/27/2016
 */
public class JsonFileIterator implements Iterator<Map<String, Object>>, Closeable {
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished = false;
    private Gson gson = new Gson();

    /**
     * Constructs an iterator of the lines for a <code>fileName</code>.
     *
     * @param fileName the <code>fileName</code> to read from
     * @throws IOException thrown if there is a problem accessing the file
     */
    public JsonFileIterator(final String fileName) throws IOException {
        this(Files.newBufferedReader(Paths.get(fileName), Charset.defaultCharset()));
    }

    /**
     * Constructs an iterator of the lines for a <code>Reader</code>.
     *
     * @param reader the <code>Reader</code> to read from, not null
     * @throws IllegalArgumentException if the reader is null
     */
    public JsonFileIterator(final Reader reader) throws IllegalArgumentException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }
    }

    /**
     * Indicates whether the <code>Reader</code> has more lines.
     * If there is an <code>IOException</code> then {@link #close()} will
     * be called on this instance.
     *
     * @return {@code true} if the Reader has more lines
     * @throws IllegalStateException if an IO exception occurs
     */
    public boolean hasNext() {
        if (cachedLine != null) {
            return true;
        } else if (finished) {
            return false;
        } else {
            try {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    finished = true;
                    return false;
                }
                cachedLine = line;
                return true;
            } catch (final IOException ioe) {
                close();
                throw new IllegalStateException(ioe);
            }
        }
    }

    /**
     * Returns the next object in the file or wrapped <code>Reader</code>.
     *
     * @return the next JSON object from the input
     * @throws NoSuchElementException if there is no object to return
     */
    public Map<String, Object> next() {
        return nextObject();
    }

    /**
     * Returns the next object in the file or wrapped <code>Reader</code>.
     *
     * @return the next JSON object from the input
     * @throws NoSuchElementException if there is no object to return
     */
    public Map<String, Object> nextObject() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more objects");
        }
        final String currentLine = cachedLine;
        cachedLine = null;
        return gson.fromJson(currentLine, HashMap.class);
    }

    /**
     * Closes the underlying <code>Reader</code> quietly.
     * This method is useful if you only want to process the first few
     * lines of a larger file. If you do not close the iterator
     * then the <code>Reader</code> remains open.
     * This method can safely be called multiple times.
     */
    public void close() {
        finished = true;
        try {
            bufferedReader.close();
        } catch (final IOException ioe) {
            // ignore
        }
        cachedLine = null;
    }

    /**
     * Unsupported.
     *
     * @throws UnsupportedOperationException always
     */
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on JsonFileIterator");
    }

}
