package com.github.filosganga.geogson.jts;

import java.io.IOException;

import com.github.filosganga.geogson.codec.CodecRegistry;
import com.github.filosganga.geogson.model.Geometry;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class JtsAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if(com.vividsolutions.jts.geom.Geometry.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new JtsGeometryAdapter(gson);
        } else {
            return null;
        }
    }

}

class JtsGeometryAdapter extends TypeAdapter<com.vividsolutions.jts.geom.Geometry> {

    private final Gson gson;

    private final CodecRegistry<com.vividsolutions.jts.geom.Geometry, Geometry<?>> codecRegistry;

    public JtsGeometryAdapter(Gson gson) {
        this.gson = gson;
        this.codecRegistry = new CodecRegistry<>();
        codecRegistry.addCodec(new PointCodec());
        codecRegistry.addCodec(new MultiPointCodec());
        codecRegistry.addCodec(new LineStringCodec());
        codecRegistry.addCodec(new LinearRingCodec());
        codecRegistry.addCodec(new MultiLineStringCodec());
        codecRegistry.addCodec(new PolygonCodec());
        codecRegistry.addCodec(new MultiPolygonCodec());
    }

    @Override
    public void write(JsonWriter out, com.vividsolutions.jts.geom.Geometry value) throws IOException {
        if (value == null || value.getCoordinates().length == 0) {
    		out.nullValue();
    		return;
    	}
    	
        Geometry<?> to = codecRegistry.toGeometry(value);

        gson.getAdapter(new TypeToken<Geometry<?>>(){}).write(out, to);
    }

    @Override
    public com.vividsolutions.jts.geom.Geometry read(JsonReader in) throws IOException {

        Geometry<?> geometry = gson.getAdapter(new TypeToken<Geometry<?>>(){}).read(in);

        return codecRegistry.fromGeometry(geometry);
    }
}
