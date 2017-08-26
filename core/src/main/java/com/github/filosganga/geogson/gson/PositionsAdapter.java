package com.github.filosganga.geogson.gson;

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

import com.github.filosganga.geogson.model.positions.AbstractPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


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
                out.value(sp.lon());
                out.value(sp.lat());
                if (!Double.isNaN(sp.alt())) {
                    out.value(sp.alt());
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


    private static Positions parsePositions(JsonReader in) throws IOException {


        Positions parsed = null;

        if (in.peek() != JsonToken.BEGIN_ARRAY) {
            throw new IllegalArgumentException("The given json is not a valid positions");
        }

        in.beginArray();
        if (in.peek() == JsonToken.NUMBER) {
            parsed = parseSinglePosition(in);
        } else if (in.peek() == JsonToken.BEGIN_ARRAY) {

            AbstractPositions.PositionsBuilder builder = null;
            while (in.hasNext()) {
                Positions p = parsePositions(in);
                if(builder == null) {
                    builder = AbstractPositions.PositionsBuilder.builderOf(p);
                }
                builder.addChild(p);
            }

            if(builder != null) {
                parsed =  builder.build();
            }
        }

        in.endArray();

        return parsed;
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
