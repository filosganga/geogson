package com.github.filosganga.geogson.model;

import java.util.*;

import com.google.gson.JsonElement;

/**
 * Feature is a collection of properties and a geometry
 *
 * Feature can contain properties with arbitrary json as the value. These values
 * are returned as JsonElements and their serialization and de-serialization is
 * left to the user.
 *
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#feature-objects.
 *
 * eg: {@code
 *     Feature f = Feature.of(polygon).withId(id)
 * }
 */
public class Feature {

    public static class Builder {

        private Geometry<?> geometry = null;
        private Map<String, JsonElement> properties = new HashMap<>();
        private Optional<String> id = Optional.empty();

        Builder(){
        }

        public Builder withGeometry(Geometry<?> geometry) {
            this.geometry = geometry;
            return this;
        }

        public Builder withProperties(Map<String, JsonElement> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder withProperty(String name, JsonElement value) {
            this.properties.put(name, value);
            return this;
        }

        public Builder withId(Optional<String> id) {
            this.id = id;
            return this;
        }

        public Builder withId(String id) {
            return withId(Optional.of(id));
        }

        public Feature build() {
            if(geometry == null) {
                throw new IllegalStateException("geometry is required to build a Feature");
            }
            return new Feature(geometry, properties, id);
        }

    }

    private static final long serialVersionUID = 1L;

    private final Geometry<?> geometry;

    // Feature properties can contain generic json objects
    private final Map<String, JsonElement> properties;

    private final Optional<String> id;

    private Optional<Integer> cachedHashCode = Optional.empty();

    private Feature(Geometry<?> geometry, Map<String, JsonElement> properties, Optional<String> id) {
        this.geometry = geometry;
        this.properties = properties;
        this.id = id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Feature feature) {
        return builder().withGeometry(feature.geometry).withProperties(feature.properties).withId(feature.id);
    }

    /**
     * Build a {@link Feature} with the given {@link Geometry}.
     *
     * @param geometry The Geometry to build Feature from
     *
     * @return An instance of Feature
     */
    public static Feature of(Geometry<?> geometry) {
        return builder().withGeometry(geometry).build();
    }

    /**
     * The Geometry of this Feature.
     *
     * @return a Geometry instance.
     */
    public Geometry<?> geometry() {
        return geometry;
    }

    /**
     * The properties of this Feature.
     *
     * @return an Map containing the properties. An empty map if not properties have been set.
     */
    public Map<String, JsonElement> properties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * The id of the Feature.
     *
     * @return Optional.absent if this Feature does not have any id. A valued Optional otherwise.
     */
    public Optional<String> id() {
        return id;
    }


    @Override
    public int hashCode() {
        if(!cachedHashCode.isPresent()) {
            cachedHashCode = Optional.of(Objects.hash(getClass(), this.id, this.geometry, this.properties));
        }

        return cachedHashCode.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Feature other = (Feature) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.properties, other.properties)
                && Objects.equals(this.geometry, other.geometry);
    }

    @Override
    public String toString() {
        return "Feature{" +
                "geometry=" + geometry +
                ", properties=" + properties +
                ", id=" + id +
                '}';
    }
}
