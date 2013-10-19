package com.github.filosganga.geogson.jts;

import java.io.IOException;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.base.Supplier;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class JtsGeometryAdapterFactory implements TypeAdapterFactory {

    private final JtsCodec jtsCodec;

    public JtsGeometryAdapterFactory(JtsCodec jtsCodec) {
        this.jtsCodec = jtsCodec;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if (com.vividsolutions.jts.geom.Geometry.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new GeometryAdapter(gson);
        } else {
            return gson.getAdapter(type);
        }

    }

    private class GeometryAdapter extends TypeAdapter<com.vividsolutions.jts.geom.Geometry> {

        private final Gson gson;

        private GeometryAdapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(JsonWriter out, final com.vividsolutions.jts.geom.Geometry src) throws IOException {

            gson.getAdapter(Geometry.class).write(out, jtsCodec.toGeometry(src).or(new Supplier<Geometry>() {
                @Override
                public Geometry get() {
                    throw new UnsupportedOperationException("It is not able to serialize " + src);
                }
            }));
        }

        @Override
        public com.vividsolutions.jts.geom.Geometry read(JsonReader in) throws IOException {
            return jtsCodec.fromGeometry(gson.getAdapter(Geometry.class).read(in)).or(new Supplier<com.vividsolutions.jts.geom.Geometry>() {
                @Override
                public com.vividsolutions.jts.geom.Geometry get() {
                    throw new UnsupportedOperationException("It is not able to deserialize the given json.");
                }
            });
        }
    }


}
