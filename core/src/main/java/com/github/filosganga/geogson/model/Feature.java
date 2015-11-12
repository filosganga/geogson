package com.github.filosganga.geogson.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;

import java.util.Optional;

/**
 * Feature is a collection of properties and a geometry
 *
 * Feature can contain properties with arbitrary json as the value. These values
 * are returned as JsonElements and their serialization and de-serialization is
 * left to the user.
 */
public class Feature {

    private final Geometry<?> geometry;

    // Feature properties can contain generic json objects
    private final ImmutableMap<String, JsonElement> properties;

    private final Optional<String> id;

    public Feature(Geometry<?> geometry, ImmutableMap<String, JsonElement> properties, Optional<String> id) {
        this.geometry = geometry;
        this.properties = properties;
        this.id = id;
    }

    public Geometry<?> geometry() {
        return geometry;
    }

    public ImmutableMap<String, JsonElement> properties() {
        return properties;
    }

    public Optional<String> id() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), this.id, this.geometry, this.properties);
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
        return Objects.equal(this.id, other.id)
                && Objects.equal(this.properties, other.properties)
                && Objects.equal(this.geometry, other.geometry);
    }
}
