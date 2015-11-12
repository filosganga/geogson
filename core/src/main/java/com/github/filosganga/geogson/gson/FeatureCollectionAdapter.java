package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.*;


/**
 * The Gson TypeAdapter to serialize/de-serialize {@link FeatureCollection} instances.
 */
public class FeatureCollectionAdapter extends TypeAdapter<FeatureCollection> {

    private final Gson gson;

    public FeatureCollectionAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, FeatureCollection value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.beginObject();
            out.name("features");
            out.beginArray();
            for(Feature feature : value.features()) {
                gson.getAdapter(Feature.class).write(out, feature);
            }
            out.endArray();
            out.endObject();
        }
    }

    @Override
    public FeatureCollection read(JsonReader in) throws IOException {
        FeatureCollection featureColelction = null;
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
        } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();

            List<Feature> features = null;

            while (in.hasNext()) {
                String name = in.nextName();
                if ("features".equals(name)) {
                    in.beginArray();
                    features = new ArrayList<>();
                    while(in.peek() == JsonToken.BEGIN_OBJECT) {
                        Feature feature = gson.fromJson(in, Feature.class);
                        features.add(feature);
                    }
                    in.endArray();
                } else {
                    // Ignore
                }
            }

            if (features == null) {
                throw new IllegalArgumentException("Required field 'features' is missing");
            }
            featureColelction = new FeatureCollection(ImmutableList.copyOf(features));
            in.endObject();

        } else {
            throw new IllegalArgumentException("The given json is not a valid FeatureCollection: " + in.peek());
        }

        return featureColelction;
    }

}
