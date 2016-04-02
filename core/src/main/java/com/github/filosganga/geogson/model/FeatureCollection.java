package com.github.filosganga.geogson.model;

import com.google.common.base.Objects;

import java.util.List;

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
        return Objects.hashCode(getClass(), this.features);
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
        return Objects.equal(this.features, other.features);
    }
}
