package com.github.filosganga.geogson.model;

import java.util.Objects;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
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

    private final Geometry<?> geometry;

    // Feature properties can contain generic json objects
    private final ImmutableMap<String, JsonElement> properties;

    private final Optional<String> id;

    public Feature(Geometry<?> geometry, ImmutableMap<String, JsonElement> properties, Optional<String> id) {
        this.geometry = geometry;
        this.properties = properties;
        this.id = id;
    }

    /**
     * Build a {@link Feature} with the given {@link Geometry}.
     *
     * @param geometry The Geometry to build Feature from
     *
     * @return An instance of Feature
     */
    public static Feature of(Geometry<?> geometry) {
        return new Feature(geometry, ImmutableMap.<String, JsonElement>of(), Optional.<String>absent());
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
     * @return an ImmutableMap containing the properties. An empty map if not properties have been set.
     */
    public ImmutableMap<String, JsonElement> properties() {
        return properties;
    }

    /**
     * The id of the Feature.
     *
     * @return Optional.absent if this Feature does not have any id. A valued Optional otherwise.
     */
    public Optional<String> id() {
        return id;
    }

    /**
     * Return a copy of this Feature with the given id.
     *
     * @param id The id of the new Feature instance.
     * @return a new Feature instance.
     */
    public Feature withId(String id) {
        return new Feature(geometry, properties, Optional.of(id));
    }

    /**
     * Return a copy of this Feature with the given properties.
     *
     * @param properties The properties of the new Feature instance.
     * @return a new Feature instance.
     */
    public Feature withProperties(ImmutableMap<String, JsonElement> properties) {
        return new Feature(geometry, properties, id);
    }

    /**
     * Return a copy of this Feature with the existing properties plus the given property.
     *
     * @param name The name of the property to add.
     * @param value The value of the property to add.
     *
     * @return a new Feature instance.
     */
    public Feature withProperty(String name, JsonElement value) {
        return new Feature(geometry, ImmutableMap.<String, JsonElement>builder().putAll(properties).put(name, value).build(), id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), this.id, this.geometry, this.properties);
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
}
