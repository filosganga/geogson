package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.Geometry;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * The Gson TypeAdapter to serialize/de-serialize {@link Feature} instances.
 */
public class FeatureAdapter extends TypeAdapter<Feature> {

    public static final String FEATURE_TYPE = "Feature";

    public static final String PROPERTIES_NAME = "properties";
    public static final String GEOMETRY_NAME = "geometry";
    public static final String ID_NAME = "id";
    public static final String TYPE_NAME = "type";
    private final Gson gson;

    public FeatureAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Feature value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.beginObject();
            if(value.id().isPresent()) {
                out.name(ID_NAME).value(value.id().get());
            }
            out.name(TYPE_NAME).value(FEATURE_TYPE);
            writeProperties(out, value);
            writeGeometry(out, value);
            out.endObject();
        }
    }

    private void writeGeometry(JsonWriter out, Feature value) throws IOException {
        out.name(GEOMETRY_NAME);
        gson.getAdapter(Geometry.class).write(out, value.geometry());
    }

    private void writeProperties(JsonWriter out, Feature value) throws IOException {
        out.name(PROPERTIES_NAME);
        out.beginObject();
        for(String key : value.properties().keySet()) {
            out.name(key);
            JsonElement propertyValue = gson.toJsonTree(value.properties().get(key));
            gson.toJson(propertyValue, out);
        }
        out.endObject();
    }

    @Override
    public Feature read(JsonReader in) throws IOException {
        Feature feature = null;
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
        } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();

            Optional<String> id = Optional.absent();
            Map<String, JsonElement> properties = null;
            Geometry<?> geometry = null;

            while (in.hasNext()) {
                String name = in.nextName();
                if (TYPE_NAME.equals(name) && !FEATURE_TYPE.equals(in.nextString())) {
                    throw new IllegalArgumentException("The given json is not a valid Feature");
                } else if (PROPERTIES_NAME.equals(name)) {
                    properties = readProperties(in);
                } else if (GEOMETRY_NAME.equals(name)) {
                    geometry = gson.fromJson(in, Geometry.class);
                } else if (ID_NAME.equals(name)) {
                    id = Optional.of(in.nextString());
                } else {
                    // Ignore
                }
            }

            if (properties == null) {
                throw new IllegalArgumentException("Required field 'properties' is missing");
            }
            if (geometry == null) {
                throw new IllegalArgumentException("Required field 'geometry' is missing");
            }

            feature = new Feature(geometry, ImmutableMap.copyOf(properties), id);

            in.endObject();

        } else {
            throw new IllegalArgumentException("The given json is not a valid Feature: " + in.peek());
        }

        return feature;
    }

    private Map<String, JsonElement> readProperties(JsonReader in) throws IOException {
        Map<String, JsonElement> result = new HashMap<>();
        JsonParser parser = new JsonParser();
        in.beginObject();
        while (in.peek() != JsonToken.END_OBJECT) {
            String name = in.nextName();
            JsonElement value = parser.parse(in);
            result.put(name, value);
        }
        in.endObject();
        return result;
    }

}
