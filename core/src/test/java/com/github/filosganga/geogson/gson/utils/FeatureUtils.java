package com.github.filosganga.geogson.gson.utils;


import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;

import java.util.Optional;

public final class FeatureUtils {

    public static Feature featureWithId(String id) {
        return new Feature(Point.from(12.3, 45.3), ImmutableMap.<String, JsonElement>of(), Optional.of(id));
    }

    public static Feature featureWithGeometry(Geometry<?> geometry) {
        return new Feature(geometry, ImmutableMap.<String, JsonElement>of(), Optional.<String>empty());
    }

    public static Feature featureWithProperties(ImmutableMap<String, JsonElement> properties) {
        return new Feature(Point.from(12.3, 45.3), properties, Optional.<String>empty());
    }

    private FeatureUtils() {
    }
}
