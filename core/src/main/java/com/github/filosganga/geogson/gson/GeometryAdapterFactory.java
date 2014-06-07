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

import static com.google.common.collect.Iterables.transform;

import java.awt.geom.Area;
import java.io.IOException;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.LinearRing;
import com.github.filosganga.geogson.model.MultiPoint;
import com.github.filosganga.geogson.model.MultiPolygon;
import com.github.filosganga.geogson.model.Point;
import com.github.filosganga.geogson.model.Polygon;
import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.github.filosganga.geogson.util.ChainableOptional;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
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
                    out.name("coordinates");
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
                    } else if ("coordinates".equals(name)) {
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
                throw new IllegalArgumentException("The given json is not a valid Geometry: " + in.peek());
            }

            return geometry;
        }

        private Positions readPosition(JsonReader in) throws IOException {
            return gson.getAdapter(Positions.class).read(in);
        }

        private Geometry buildGeometry(final String type, Positions positions, Iterable<Geometry> geometries) {

            // Take care, the order is important!
            return ChainableOptional
                    .of(buildGeometryCollection(type, geometries))
                    .or(buildMultiPolygon(type, positions))
                    .or(buildPolygon(type, positions))
                    .or(buildLinearRing(type, positions))
                    .or(buildLineString(type, positions))
                    .or(buildMultiPoint(type, positions))
                    .or(buildPoint(type, positions))
                    .orFinally(new Supplier<Geometry>() {
                        @Override
                        public Geometry get() {
                            throw new IllegalArgumentException("Cannot build a geometry for type: " + type);
                        }
                    });
        }

        private Supplier<Optional<? extends Geometry>> buildPoint(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry>>() {
                @Override
                public Optional<Geometry> get() {
                    Optional<Geometry> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.POINT.getValue())) {
                        mayGeometry = Optional.<Geometry>of(Point.from(((SinglePosition) coordinates).coordinates()));
                    }

                    return mayGeometry;
                }
            };


        }

        private Supplier<Optional<? extends Geometry>> buildMultiPoint(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry>>() {
                @Override
                public Optional<Geometry> get() {
                    Optional<Geometry> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.MULTI_POINT.getValue())) {
                        mayGeometry = Optional.<Geometry>of(new MultiPoint((LinearPositions) coordinates));
                    }

                    return mayGeometry;
                }
            };

        }

        private Supplier<Optional<? extends Geometry>> buildLineString(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry>>() {
                @Override
                public Optional<Geometry> get() {
                    Optional<Geometry> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.LINE_STRING.getValue())) {
                        mayGeometry = Optional.<Geometry>of(new LineString((LinearPositions) coordinates));
                    }

                    return mayGeometry;
                }
            };
        }

        private Supplier<Optional<? extends Geometry>> buildLinearRing(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry>>() {
                @Override
                public Optional<Geometry> get() {
                    Optional<Geometry> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.LINEAR_RING.getValue())) {
                        LinearPositions linearPositions = (LinearPositions) coordinates;
                        if (linearPositions.isClosed()) {
                            mayGeometry = Optional.<Geometry>of(new LinearRing(linearPositions));
                        }
                    }

                    return mayGeometry;
                }
            };


        }

        private Supplier<Optional<? extends Geometry>> buildPolygon(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry>>() {
                @Override
                public Optional<Geometry> get() {
                    Optional<Geometry> mayGeometry = Optional.absent();

                    if (Geometry.Type.POLYGON.getValue().equalsIgnoreCase(type)) {

                        AreaPositions positions;
                        if (coordinates instanceof LinearPositions) {
                            LinearPositions lp = (LinearPositions) coordinates;
                            positions = new AreaPositions(ImmutableList.of(lp));
                        } else {
                            positions = (AreaPositions) coordinates;
                        }

                        mayGeometry = Optional.<Geometry>of(new Polygon(positions));
                    }

                    return mayGeometry;
                }
            };


        }

        private Supplier<Optional<? extends Geometry>> buildMultiPolygon(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry>>() {
                @Override
                public Optional<Geometry> get() {
                    Optional<Geometry> mayGeometry = Optional.absent();
                    if (Geometry.Type.MULTI_POLYGON.getValue().equalsIgnoreCase(type)) {

                        MultiDimensionalPositions positions;
                        if (coordinates instanceof AreaPositions) {
                            AreaPositions ap = (AreaPositions) coordinates;

                            positions = new MultiDimensionalPositions(transform(ap.children(), new Function<LinearPositions, AreaPositions>() {
                                @Override
                                public AreaPositions apply(LinearPositions input) {
                                    return new AreaPositions(ImmutableList.of(input));
                                }
                            }));
                        } else {
                            positions = (MultiDimensionalPositions) coordinates;
                        }

                        mayGeometry = Optional.<Geometry>of(new MultiPolygon(positions));
                    }

                    return mayGeometry;
                }
            };
        }

        private Supplier<Optional<? extends Geometry>> buildGeometryCollection(final String type, final Iterable<Geometry> geometries) {

            return new Supplier<Optional<? extends Geometry>>() {
                @Override
                public Optional<Geometry> get() {

                    // TODO Not supported at this time

                    return Optional.absent();
                }
            };

        }

    }
}
