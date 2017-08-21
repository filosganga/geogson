package com.github.filosganga.geogson;


import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class PerformaceTest {

    public static void main(String[] args) {
        PerformaceTest pt = new PerformaceTest();
        while (true) {
            pt.featureCollectionReading();
        }
    }

    private static Reader openReader(String resourceName) throws IOException {
        return new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(resourceName)));
    }

    @Benchmark
    public void featureCollectionReading() {

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();

        try(Reader reader = openReader("feature-collection.json")) {
            FeatureCollection parsed = gson.fromJson(reader, FeatureCollection.class);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }
}
