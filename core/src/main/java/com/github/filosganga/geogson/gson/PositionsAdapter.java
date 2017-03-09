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

import com.github.filosganga.geogson.model.Coordinates;
import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
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

    private Positions parsePositions(JsonReader in) throws IOException {

        Optional<Positions> parsed = Optional.absent();

        if (in.peek() != JsonToken.BEGIN_ARRAY) {
            throw new IllegalArgumentException("The given json is not a valid positions");
        }

        in.beginArray();
        if (in.peek() == JsonToken.NUMBER) {
            parsed = Optional.of(parseSinglePosition(in));
        } else if (in.peek() == JsonToken.BEGIN_ARRAY) {
            while (in.hasNext()) {
                Positions thisPositions = parsePositions(in);
                // fix bug #30: according to the recursion (i.e. the array structure;
                // recognize that we came from a recursion because no parsed has no
                // value yet): convert the already parsed Positions to the
                // LinearPositions/AreaPositions matching the recursion level
                if (parsed.equals(Optional.absent()) && thisPositions instanceof LinearPositions) {
                    AreaPositions areaPositions = new AreaPositions(ImmutableList.of((LinearPositions) thisPositions));
                    parsed = Optional.of((Positions) areaPositions);
                } else if (parsed.equals(Optional.absent()) && thisPositions instanceof AreaPositions) {
                    MultiDimensionalPositions multiPositions = new MultiDimensionalPositions(ImmutableList.of((AreaPositions) thisPositions));
                    parsed = Optional.of((Positions) multiPositions);
                } else {
                  // mergeFn() does all the rest, if parsed has a value
                  parsed = parsed.transform(mergeFn(thisPositions)).or(Optional.of(thisPositions));
                }

            }
        }

        in.endArray();

        return parsed.orNull();
    }

    private Function<Positions, Positions> mergeFn(final Positions p) {
        return new Function<Positions, Positions>() {
            @Override
            public Positions apply(Positions input) {
                return input.merge(p);
            }
        };
    }

    private Positions parseSinglePosition(JsonReader in) throws IOException {
        Positions parsed;
        double lon = in.nextDouble();
        double lat = in.nextDouble();
        double alt = Double.NaN;

        if (in.hasNext()) {
            alt = in.nextDouble();
        }

        // Skip eventual altitude or other dimensions
        while (in.peek() != JsonToken.END_ARRAY) {
            in.skipValue();
        }

        parsed = new SinglePosition(Coordinates.of(lon, lat, alt));
        return parsed;
    }

}
