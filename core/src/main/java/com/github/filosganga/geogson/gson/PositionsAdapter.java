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
import java.util.LinkedList;
import java.util.Optional;

import com.github.filosganga.geogson.model.Coordinates;
import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


/**
 * The Gson TypeAdapter to serialize/de-serialize all the {@link Positions} instances.
 */
public class PositionsAdapter extends TypeAdapter<Positions> {

    @Override
    public void write(JsonWriter out, Positions value) throws IOException {

        if (value == null) {
            out.nullValue();
        } else {
            out.beginArray();
            if (value instanceof SinglePosition) {
                SinglePosition sp = (SinglePosition) value;
                out.value(sp.coordinates().getLon());
                out.value(sp.coordinates().getLat());
                if (!Double.isNaN(sp.coordinates().getAlt())) {
                    out.value(sp.coordinates().getAlt());
                }
            } else {
                for (Positions child : value.children()) {
                    write(out, child);
                }
            }
            out.endArray();
        }
    }

    @Override
    public Positions read(JsonReader in) throws IOException {

        Positions parsed;

        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            in.nextNull();
            parsed = null;
        } else if (peek == JsonToken.BEGIN_ARRAY) {
            parsed = parsePositions(in);
        } else {
            throw new IllegalArgumentException("The json must be an array or null: " + in.peek());
        }

        return parsed;
    }


    private static <P extends Positions> P parsePositions(JsonReader in) throws IOException {


        Optional<P> parsed = Optional.empty();

        if (in.peek() != JsonToken.BEGIN_ARRAY) {
            throw new IllegalArgumentException("The given json is not a valid positions");
        }

        in.beginArray();
        if (in.peek() == JsonToken.NUMBER) {
            parsed = Optional.of((P)parseSinglePosition(in));
        } else if (in.peek() == JsonToken.BEGIN_ARRAY) {
            LinkedList<P> innerPositions = new LinkedList<>();

            int size = 0;
            P first = null;
            P last = null;

            while (in.hasNext()) {
                last = parsePositions(in);
                if(first == null) {
                    first = last;
                }
                size ++;

                innerPositions.add(last);
            }

            boolean isClosed = size >=4 && last.equals(first);

            if(innerPositions.getFirst() instanceof SinglePosition) {
                parsed = Optional.of((P)new LinearPositions((Iterable<SinglePosition>) innerPositions, isClosed));
            } else if(innerPositions.getFirst() instanceof LinearPositions) {
                parsed = Optional.of((P)new AreaPositions((Iterable<LinearPositions>) innerPositions));
            } else if (innerPositions.getFirst() instanceof AreaPositions) {
                parsed = Optional.of((P)new MultiDimensionalPositions((Iterable<AreaPositions>) innerPositions));
            }
        }

        in.endArray();

        return parsed.orElse(null);
    }

    private static Positions parseSinglePosition(JsonReader in) throws IOException {
        double lon = in.nextDouble();
        double lat = in.nextDouble();
        double alt = Double.NaN;

        if (in.hasNext()) {
            alt = in.nextDouble();
        }

        // Skip eventual other dimensions
        while (in.peek() != JsonToken.END_ARRAY) {
            in.skipValue();
        }

        return new SinglePosition(lon, lat, alt);
    }

}
