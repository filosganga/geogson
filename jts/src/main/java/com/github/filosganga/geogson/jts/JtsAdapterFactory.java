package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.codec.CodecRegistry;
import com.github.filosganga.geogson.model.Geometry;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.IOException;

/**
 * The JTS Gson TypeAdapterFactory.
 *
 * It can be built passing a GeometryFactory instance, otherwise it will instantiate a default one.
 *
 * How to use it:
 * <pre>
 *     Gson gson = new GsonBuilder()
 *         .registerTypeAdapterFactory(new JtsAdapterFactory(new GeometryFactory(new PrecisionModel(100), 4326)))
 *         .registerTypeAdapterFactory(new GeometryAdapterFactory())
 *         .create();
 * </pre>
 *
 */
public class JtsAdapterFactory implements TypeAdapterFactory {

    /**
     * {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    private final GeometryFactory geometryFactory;

    /**
     * Create an adapter for {@link org.locationtech.jts.geom.Geometry JTS
     * Geometry} with a default {@link GeometryFactory} (i.e. using the
     * {@link GeometryFactory#GeometryFactory() empty constructor})
     *
     */
    public JtsAdapterFactory() {
        this.geometryFactory = new GeometryFactory();
    }

    /**
     * Create an adapter for {@link org.locationtech.jts.geom.Geometry JTS
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

        if(org.locationtech.jts.geom.Geometry.class.isAssignableFrom(type.getRawType())) {
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

class JtsGeometryAdapter extends TypeAdapter<org.locationtech.jts.geom.Geometry> {

    private final Gson gson;

    private final CodecRegistry<org.locationtech.jts.geom.Geometry, Geometry<?>> codecRegistry;

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
        codecRegistry.addCodec(new GeometryCollectionCodec(geometryFactory));
    }

    @Override
    public void write(JsonWriter out, org.locationtech.jts.geom.Geometry value) throws IOException {
        if (value == null || value.getCoordinates().length == 0) {
            out.nullValue();
            return;
        }
        Geometry<?> to = codecRegistry.toGeometry(value);

        gson.getAdapter(new TypeToken<Geometry<?>>(){}).write(out, to);
    }

    @Override
    public org.locationtech.jts.geom.Geometry read(JsonReader in) throws IOException {

        Geometry<?> geometry = gson.getAdapter(new TypeToken<Geometry<?>>(){}).read(in);

        if (geometry == null) {
          return null;
        }
        return codecRegistry.fromGeometry(geometry);
    }
}