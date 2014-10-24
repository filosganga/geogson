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
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class JtsAdapterFactory implements TypeAdapterFactory {

    /**
     * {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    private final GeometryFactory geometryFactory;

    /**
     * Create an adapter for {@link com.vividsolutions.jts.geom.Geometry JTS
     * Geometry} with a default {@link GeometryFactory} (i.e. using the
     * {@link GeometryFactory#GeometryFactory() empty constructor})
     *
     */
    public JtsAdapterFactory() {
        this.geometryFactory = new GeometryFactory();
    }

    /**
     * Create an adapter for {@link com.vividsolutions.jts.geom.Geometry JTS
     * Geometry} with an optional {@link GeometryFactory}
     *
     * @param geometryFactory
     *          an optional {@link GeometryFactory} defining a PrecisionModel
     *          and a SRID
     */
    public JtsAdapterFactory(GeometryFactory geometryFactory) {
        if (geometryFactory == null) {
            this.geometryFactory = new GeometryFactory();
        } else {
            this.geometryFactory = geometryFactory;
        }
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if(com.vividsolutions.jts.geom.Geometry.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new JtsGeometryAdapter(gson, getGeometryFactory());
        } else {
            return null;
        }
    }

    /**
     * Get the {@link GeometryFactory} of this {@link JtsAdapterFactory}
     *
     * @return the {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public GeometryFactory getGeometryFactory() {
        return this.geometryFactory;
    }

}

class JtsGeometryAdapter extends TypeAdapter<com.vividsolutions.jts.geom.Geometry> {

    private final Gson gson;

    private final CodecRegistry<com.vividsolutions.jts.geom.Geometry, Geometry<?>> codecRegistry;

    public JtsGeometryAdapter(Gson gson, GeometryFactory geometryFactory) {
        this.gson = gson;
        this.codecRegistry = new CodecRegistry<>();
        codecRegistry.addCodec(new PointCodec(geometryFactory));
        codecRegistry.addCodec(new MultiPointCodec(geometryFactory));
        codecRegistry.addCodec(new LineStringCodec(geometryFactory));
        codecRegistry.addCodec(new LinearRingCodec(geometryFactory));
        codecRegistry.addCodec(new MultiLineStringCodec(geometryFactory));
        codecRegistry.addCodec(new PolygonCodec(geometryFactory));
        codecRegistry.addCodec(new MultiPolygonCodec(geometryFactory));
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

        if (geometry == null) {
          return null;
        }
        return codecRegistry.fromGeometry(geometry);
    }
}