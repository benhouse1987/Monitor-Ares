package com.caffinc.jaggr.core.entities;

/**
 * Holds a pair of values
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class Tuple2<T1, T2> {
    public T1 _1;
    public T2 _2;

    public Tuple2(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }
}
