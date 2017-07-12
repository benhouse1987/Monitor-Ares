package com.caffinc.jaggr.core.entities;

/**
 * Holds a 3-tuple of values
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class Tuple3<T1, T2, T3> {
    public T1 _1;
    public T2 _2;
    public T3 _3;

    public Tuple3(T1 _1, T2 _2, T3 _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }
}
