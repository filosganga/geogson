package com.github.filosganga.geogson.gson.utils;


import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;
import com.google.gson.JsonElement;

import java.util.Map;

public final class FeatureUtils {

    public static Feature featureWithId(String id) {
        return Feature.builder().withGeometry(Point.from(12.3, 45.3)).withId(id).build();
    }

    public static Feature featureWithGeometry(Geometry<?> geometry) {
        return Feature.builder().withGeometry(Point.from(12.3, 45.3)).build();
    }

    public static Feature featureWithProperties(Map<String, JsonElement> properties) {
        return Feature.builder().withGeometry(Point.from(12.3, 45.3)).withProperties(properties).build();
    }

    private FeatureUtils() {
    }
}
