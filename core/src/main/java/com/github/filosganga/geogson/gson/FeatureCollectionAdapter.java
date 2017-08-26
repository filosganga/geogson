package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
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
public final class FeatureCollectionAdapter extends TypeAdapter<FeatureCollection> {

    private final Gson gson;
    private final TypeAdapter<Feature> featureAdapter;

    public FeatureCollectionAdapter(Gson gson) {
        this.gson = gson;
        this.featureAdapter = gson.getAdapter(Feature.class);
    }

    @Override
    public void write(JsonWriter out, FeatureCollection value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.beginObject();
            out.name("type").value("FeatureCollection");
            out.name("features");
            out.beginArray();
            for(Feature feature : value.features()) {
                featureAdapter.write(out, feature);
            }
            out.endArray();
            out.endObject();
        }
    }

    @Override
    public FeatureCollection read(JsonReader in) throws IOException {
        FeatureCollection featureCollection = null;

        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
        } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();
            LinkedList<Feature> features = null;

            while (in.hasNext()) {
                String name = in.nextName();
                if ("features".equalsIgnoreCase(name)) {
                    in.beginArray();
                    features = new LinkedList<>();
                    while(in.peek() == JsonToken.BEGIN_OBJECT) {
                        Feature feature = gson.fromJson(in, Feature.class);
                        features.add(feature);
                    }
                    in.endArray();
                } else {
                    in.skipValue();
                }
            }

            if (features == null) {
                throw new IllegalArgumentException("Required field 'features' is missing");
            }

            featureCollection = new FeatureCollection(features);
            in.endObject();

        } else {
            throw new IllegalArgumentException("The given json is not a valid FeatureCollection: " + in.peek());
        }

        return featureCollection;
    }

}
