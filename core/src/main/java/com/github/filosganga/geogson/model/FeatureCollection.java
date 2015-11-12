package com.github.filosganga.geogson.model;

import java.util.List;

/**
 * FeatureCollection contains a list of {@link Feature} instances
 */
public class FeatureCollection {
    private final List<Feature> features;

    public FeatureCollection(List<Feature> features) {
        this.features = features;
    }

    public List<Feature> features() {
        return features;
    }
}
