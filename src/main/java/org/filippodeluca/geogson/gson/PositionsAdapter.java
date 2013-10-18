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

package org.filippodeluca.geogson.gson;

import java.io.IOException;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.filippodeluca.geogson.model.Position;
import org.filippodeluca.geogson.model.Positions;
import org.filippodeluca.geogson.model.SinglePosition;


/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PositionsAdapter extends TypeAdapter<Positions> {

    @Override
    public void write(JsonWriter out, Positions value) throws IOException {

        if(value == null) {
            out.nullValue();
        } else {
            // TODO
        }
    }

    @Override
    public Positions read(JsonReader in) throws IOException {

        Positions parsed;

        JsonToken peek = in.peek();
        if(peek == JsonToken.NULL) {
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

        if(in.peek() != JsonToken.BEGIN_ARRAY) {
            throw new IllegalArgumentException("The given json is not a valid positions");
        }

        in.beginArray();
        if(in.peek() == JsonToken.NUMBER) {
            parsed = Optional.of(parseSinglePosition(in));
        } else if(in.peek() == JsonToken.BEGIN_ARRAY) {
            while (in.hasNext()) {
                Positions thisPositions = parsePositions(in);
                parsed = parsed.transform(mergeFn(thisPositions)).or(Optional.of(thisPositions));
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

        // Skip eventual altitude or other dimensions
        while (in.peek() != JsonToken.END_ARRAY) {
            in.skipValue();
        }

        parsed = new SinglePosition(Position.of(lon, lat));
        return parsed;
    }

}
