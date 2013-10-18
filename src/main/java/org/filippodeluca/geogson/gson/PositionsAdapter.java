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
