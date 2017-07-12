package com.caffinc.jaggr.core.utils;

import java.util.Map;

/**
 * Extracts a nested field value from a Map
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class FieldValueExtractor {

    /**
     * Extracts a nested value for the specified field from the given object
     *
     * @param field  Field to extract
     * @param object Object to extract from
     * @return Extracted value
     */
    public static Object getValue(String field, Map<String, Object> object) {
        return getValue(field.split("\\."), object);
    }

    /**
     * Extracts a nested value for the specified nested field from the given object
     *
     * @param split  Nested field to extract
     * @param object Object to extract from
     * @return Extracted value
     */
    public static Object getValue(String[] split, Map<String, Object> object) {
        return getValue(split, 0, object);
    }


    public static Object getCombinedValue(String keys, Map<String, Object> object) {

        String[] keyGroup=keys.split("@@");
        String value="";
        for (String s:keyGroup){
            String[] splitKey=s.split("\\.");
            value+= "@@"+ getValue(splitKey, 0, object);
        }

        return value;
    }

    private static Object getValue(String[] split, int i, Map<String, Object> object) {
        if (i == split.length - 1) {
            // This is the value we need, extract it
            return object.get(split[i]);
        } else {
            // Go deeper if possible, else return null
            if (object.containsKey(split[i]) && object.get(split[i]) instanceof Map) {
                return getValue(split, i + 1, (Map<String, Object>) object.get(split[i]));
            } else {
                return null;
            }
        }
    }
}
