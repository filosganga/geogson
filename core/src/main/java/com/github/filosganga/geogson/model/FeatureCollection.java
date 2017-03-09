package com.github.filosganga.geogson.model;

import java.util.List;
import java.util.Objects;

/**
 * FeatureCollection contains a list of {@link Feature} instances
 *
 * GeoJson reference @see http://geojson.org/geojson-spec.html#feature-collection-objects.
 *
 *
 */
public class FeatureCollection {
    private final List<Feature> features;

    public FeatureCollection(List<Feature> features) {
        this.features = features;
    }

    public List<Feature> features() {
        return features;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), this.features);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FeatureCollection other = (FeatureCollection) obj;
        return Objects.equals(this.features, other.features);
    }
}
