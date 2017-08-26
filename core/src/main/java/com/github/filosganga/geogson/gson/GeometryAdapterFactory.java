/*
 * Copyright 2013 Filippo De Luca - me@filippodeluca.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.model.*;
import com.github.filosganga.geogson.model.positions.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

/**
 * The Gson TypeAdapterFactory responsible to serialize/de-serialize all the {@link Geometry}, {@link Feature}
 * and {@link FeatureCollection} instances.
 */
public final class GeometryAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (Geometry.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new GeometryAdapter(gson);
        } else if (Positions.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new PositionsAdapter();
        } else if (Feature.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new FeatureAdapter(gson);
        } else if (FeatureCollection.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new FeatureCollectionAdapter(gson);
        } else {
            return null;
        }
    }

    private static class GeometryAdapter extends TypeAdapter<Geometry> {

        private final TypeAdapter<Geometry> geometryAdapter;
        private final TypeAdapter<Positions> positionsAdapter;

        private GeometryAdapter(Gson gson) {
            this.geometryAdapter = gson.getAdapter(Geometry.class);
            this.positionsAdapter = gson.getAdapter(Positions.class);
        }

        @Override
        public void write(JsonWriter out, Geometry value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginObject();

                out.name("type").value(value.type().getValue());
                if (value.type() == Geometry.Type.GEOMETRY_COLLECTION) {
                    out.name("geometries");
                    out.beginArray();
                    GeometryCollection geometries = (GeometryCollection) value;
                    for (Geometry<?> geometry : geometries.getGeometries()) {
                        geometryAdapter.write(out, geometry);
                    }
                    out.endArray();
                } else {
                    out.name("coordinates");
                    positionsAdapter.write(out, value.positions());
                }
                out.endObject();
            }
        }

        @Override
        public Geometry<?> read(JsonReader in) throws IOException {

            Geometry<?> geometry = null;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
            } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
                in.beginObject();

                Geometry.Type type = null;
                Positions positions = null;
                Geometry<?> geometries = null;

                while (in.hasNext()) {
                    String name = in.nextName();
                    if ("type".equals(name)) {
                        type = Geometry.Type.forValue(in.nextString());
                    } else if ("coordinates".equals(name)) {
                        positions = positionsAdapter.read(in);
                    } else if ("geometries".equals(name)) {
                        geometries = readGeometries(in);
                    } else {
                        in.skipValue();
                    }
                }

                geometry = buildGeometry(type, positions, geometries);

                in.endObject();

            } else {
                throw new IllegalArgumentException("The given json is not a valid Geometry: " + in.peek());
            }

            return geometry;
        }


        private Geometry<?> readGeometries(JsonReader in) throws IOException {
            Geometry<?> parsed;

            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                parsed = null;
            } else if (peek == JsonToken.BEGIN_ARRAY) {
                parsed = parseGeometries(in);
            } else {
                throw new IllegalArgumentException("The json must be an array or null: " + in.peek());
            }

            return parsed;
        }

        private Geometry<?> parseGeometries(JsonReader in) throws IOException {

            Optional<Geometry<?>> parsed = Optional.empty();

            if (in.peek() != JsonToken.BEGIN_ARRAY) {
                throw new IllegalArgumentException("The given json is not a valid GeometryCollection");
            }

            in.beginArray();
            if (in.peek() == JsonToken.BEGIN_OBJECT) {
                LinkedList<Geometry<?>> geometries = new LinkedList<>();
                while (in.hasNext()) {
                    @SuppressWarnings("rawtypes")
                    Geometry geometry = geometryAdapter.read(in);
                    geometries.add(geometry);
                }
                parsed = Optional.<Geometry<?>>of(GeometryCollection.of(geometries));
            }

            in.endArray();

            return parsed.orElse(null);
        }

        private Geometry<?> buildGeometry(final Geometry.Type type, final Positions positions, final Geometry<?> geometries) {
            switch (type) {
                case GEOMETRY_COLLECTION:
                    return geometries;
                case MULTI_POLYGON:
                    return new MultiPolygon((MultiDimensionalPositions) positions);
                case POLYGON:
                    return new Polygon((AreaPositions) positions);
                case MULTI_LINE_STRING:
                    return new MultiLineString((AreaPositions) positions);
                case LINEAR_RING:
                case LINE_STRING:
                    return ((LinearPositions) positions).isClosed()
                            ? new LinearRing((LinearPositions) positions)
                            : new LineString((LinearPositions) positions);
                case MULTI_POINT:
                    if (positions instanceof SinglePosition) {
                        return new MultiPoint(LinearPositions.builder().addSinglePosition((SinglePosition) positions).build());
                    } else {
                        return new MultiPoint((LinearPositions) positions);
                    }
                case POINT:
                    return new Point(((SinglePosition) positions));
                default:
                    throw new IllegalArgumentException("Cannot build a geometry for type: " + type);
            }
        }


    }
}
