package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.gson.utils.FeatureUtils;
import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class FeatureCollectionAdapterTest {

    private Gson toTest;

    @Before
    public void initToTest() {
        toTest = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
    }

    @Test
    public void shouldHandleEmptyFeatureCollection() {
        FeatureCollection collection = new FeatureCollection(Collections.<Feature>emptyList());
        final String json = toTest.toJson(collection);
        assertThat(json, is("{\"type\":\"FeatureCollection\",\"features\":[]}"));
        FeatureCollection parsed = toTest.fromJson(json, FeatureCollection.class);
        assertThat(parsed, equalTo(collection));
    }

    @Test
    public void shouldHandleFeatureCollectionWithFeatures() {
        List<Feature> features = Arrays.asList(
                FeatureUtils.featureWithId("test1"),
                FeatureUtils.featureWithId("test2")
        );

        FeatureCollection collection = new FeatureCollection(features);
        FeatureCollection parsed = toTest.fromJson(toTest.toJson(collection), FeatureCollection.class);
        assertThat(parsed.features(), hasSize(2));
        assertThat(parsed.features().get(0).id().get(), equalTo("test1"));
        assertThat(parsed.features().get(1).id().get(), equalTo("test2"));
    }

    @Test
    public void shouldParseFeatureCollection() {

        String json = "{ \"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"} }, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ] }, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0 } }, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ] }, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"} } } ] }";

        FeatureCollection parsed = toTest.fromJson(json, FeatureCollection.class);

        assertThat(parsed.features(), hasSize(3));

    }

    @Test
    public void shouldParseRealFeatureCollection() throws IOException {

        String json = readJson("feature-collection.json");

        FeatureCollection parsed = toTest.fromJson(json, FeatureCollection.class);

        assertThat(parsed.features(), hasSize(202));

    }

    public static String readJson(String resource) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(resource)))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }


}
