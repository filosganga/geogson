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
        Feature.Builder builder = Feature.builder();
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
        } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();

            while (in.hasNext()) {
                String name = in.nextName();
                if (TYPE_NAME.equals(name)) {
                    String value = in.nextString();
                    if(!value.equalsIgnoreCase(FEATURE_TYPE)) {
                        throw new IllegalArgumentException("The given json is not a valid Feature, the type must be Feature, current type=" + value);
                    }
                } else if (PROPERTIES_NAME.equals(name)) {
                    readProperties(in, builder);
                } else if (GEOMETRY_NAME.equals(name)) {
                    Geometry<?> geometry = gson.fromJson(in, Geometry.class);
                    builder.withGeometry(geometry);
                } else if (ID_NAME.equals(name)) {
                    builder.withId(Optional.fromNullable(in.nextString()));
                } else {
                    // Skip unknown value.
                    in.skipValue();
                }
            }

            in.endObject();

        } else {
            throw new IllegalArgumentException("The given json is not a valid Feature: " + in.peek());
        }

        return builder.build();
    }

    private void readProperties(JsonReader in, Feature.Builder builder) throws IOException {
        JsonParser parser = new JsonParser();
        in.beginObject();
        while (in.peek() != JsonToken.END_OBJECT) {
            String name = in.nextName();
            JsonElement value = parser.parse(in);
            builder.withProperty(name, value);
        }
        in.endObject();
    }

}
