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

import java.io.IOException;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.MultiPoint;
import com.github.filosganga.geogson.model.MultiPolygon;
import com.github.filosganga.geogson.model.Point;
import com.github.filosganga.geogson.model.Polygon;
import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class GeometryAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (Geometry.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new GeometryAdapter(gson);
        } else if (Positions.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new PositionsAdapter();
        } else {
            return gson.getAdapter(type);
        }
    }

    private static class GeometryAdapter extends TypeAdapter<Geometry> {

        private final Gson gson;

        private GeometryAdapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(JsonWriter out, Geometry value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginObject();

                out.name("type").value(value.type().getValue());
                if (value.type() != Geometry.Type.GEOMETRY_COLLECTION) {
                    out.name("positions");
                    gson.getAdapter(Positions.class).write(out, value.positions());
                } else {
                    // TODO
                }

                out.endObject();
            }
        }

        @Override
        public Geometry read(JsonReader in) throws IOException {

            Geometry geometry = null;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
            } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
                in.beginObject();

                String type = null;
                Positions positions = null;
                Iterable<Geometry> geometries = null;

                while (in.hasNext()) {
                    String name = in.nextName();
                    if ("type".equals(name)) {
                        type = in.nextString();
                    } else if ("positions".equals(name)) {
                        positions = readPosition(in);
                    } else if ("geometries".equals(name)) {
                        // TODO
                    } else {
                        // Ignore
                    }
                }

                geometry = buildGeometry(type, positions, geometries);

                in.endObject();
            } else {
                throw new IllegalArgumentException("The give json is not a valid Geometry: " + in.peek());
            }

            return geometry;
        }

        private Positions readPosition(JsonReader in) throws IOException {
            return gson.getAdapter(Positions.class).read(in);
        }

        private Geometry buildGeometry(final String type, Positions positions, Iterable<Geometry> geometries) {
            return buildPoint(type, positions)
                    .or(buildMultiPoint(type, positions))
                    .or(buildLineString(type, positions))
                    .or(buildPolygon(type, positions))
                    .or(buildMultiPolygon(type, positions))
                    .or(buildGeometryCollection(type, geometries))
                    .or(new Supplier<Geometry>() {
                        @Override
                        public Geometry get() {
                            throw new IllegalArgumentException("Cannot build a geometry for type: " + type);
                        }
                    });
        }

        private Optional<Geometry> buildPoint(String type, Positions coordinates) {

            Optional<Geometry> mayGeometry = Optional.absent();

            if (type.equals(Geometry.Type.POINT.getValue())) {
                mayGeometry = Optional.<Geometry>of(Point.from(((SinglePosition) coordinates).coordinates()));
            }

            return mayGeometry;
        }

        private Optional<Geometry> buildMultiPoint(String type, Positions coordinates) {

            Optional<Geometry> mayGeometry = Optional.absent();

            if (type.equals(Geometry.Type.MULTI_POINT.getValue())) {
                mayGeometry = Optional.<Geometry>of(new MultiPoint((LinearPositions) coordinates));
            }

            return mayGeometry;
        }

        private Optional<Geometry> buildLineString(String type, Positions coordinates) {

            Optional<Geometry> mayGeometry = Optional.absent();

            if (type.equals(Geometry.Type.LINE_STRING.getValue())) {
                mayGeometry = Optional.<Geometry>of(new LineString((LinearPositions) coordinates));
            }

            return mayGeometry;
        }

        private Optional<Geometry> buildPolygon(String type, Positions coordinates) {

            Optional<Geometry> mayGeometry = Optional.absent();

            if (Geometry.Type.POLYGON.getValue().equals(type)) {

                mayGeometry = Optional.<Geometry>of(new Polygon((AreaPositions) coordinates));
            }


            return mayGeometry;
        }

        private Optional<Geometry> buildMultiPolygon(String type, Positions coordinates) {

            Optional<Geometry> mayGeometry = Optional.absent();
            if (Geometry.Type.MULTI_POLYGON.getValue().equals(type)) {
                mayGeometry = Optional.<Geometry>of(new MultiPolygon((MultiDimensionalPositions) coordinates));
            }


            return mayGeometry;
        }

        private Optional<Geometry> buildGeometryCollection(String type, Iterable<Geometry> geometries) {

            Optional<Geometry> mayGeometry = Optional.absent();


            return mayGeometry;
        }

    }
}
