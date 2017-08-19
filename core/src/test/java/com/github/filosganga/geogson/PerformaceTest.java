package com.github.filosganga.geogson;


import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;
import java.io.Reader;

public class PerformaceTest {

    public static void main(String[] args) {
        PerformaceTest pt = new PerformaceTest();
        while (true) {
            pt.featureCollectionReading();
        }
    }

    private static Reader openReader(String resourceName) throws IOException {
        return Resources.asCharSource(Resources.getResource(resourceName), Charsets.UTF_8).openStream();
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
