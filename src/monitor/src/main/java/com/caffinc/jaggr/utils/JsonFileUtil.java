package com.caffinc.jaggr.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads a JSON file
 *
 * @author Sriram
 * @since 11/26/2016
 */
public class JsonFileUtil {
    private static final Gson gson = new Gson();

    /**
     * Reads lines from a resource file and converts it to JSON
     *
     * @param resourceName Resource to read from
     * @return List of JSON lines from the file
     * @throws IOException thrown if there is a problem accessing the file
     */
    public static List<Map<String, Object>> readJsonFromResource(final String resourceName) throws IOException {
        List<Map<String, Object>> jsonList = new ArrayList<>();
        String json;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(JsonFileUtil.class.getClassLoader().getResourceAsStream(resourceName)))) {
            while ((json = br.readLine()) != null) {
                jsonList.add(gson.fromJson(json, HashMap.class));
            }
        }
        return jsonList;
    }

    /**
     * Reads lines from a text file and converts it to JSON
     *
     * @param fileName File name to read from
     * @return List of JSON lines from the file
     * @throws IOException thrown if there is a problem accessing the file
     */
    public static List<Map<String, Object>> readJsonFromFile(final String fileName) throws IOException {
        List<Map<String, Object>> jsonList = new ArrayList<>();
        for (String json : getFileLines(fileName)) {
            try {
                jsonList.add(gson.fromJson(json, HashMap.class));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return jsonList;
    }

    /**
     * Reads lines from a text file
     *
     * @param fileName File name to read from
     * @return List of lines from the file
     * @throws IOException thrown if there is a problem accessing the file
     */
    public static List<String> getFileLines(final String fileName) throws IOException {
        List<String> text = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.add(line);
            }
        }
        return text;
    }
}
