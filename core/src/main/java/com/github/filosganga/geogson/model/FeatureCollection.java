package com.github.filosganga.geogson.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * FeatureCollection contains a list of {@link Feature} instances
 *
 * GeoJson reference @see http://geojson.org/geojson-spec.html#feature-collection-objects.
 *
 *
 */
public class FeatureCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Feature> features;

    public FeatureCollection(List<Feature> features) {
        this.features = features;
    }

    public static FeatureCollection of(Feature...features) {
        return new FeatureCollection(Arrays.asList(features));
    }

    public List<Feature> features() {
        return Collections.unmodifiableList(features);
    }

    public int size() {
        return features.size();
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

    @Override
    public String toString() {
        return "FeatureCollection{" +
                "features=" + features +
                '}';
    }
}
