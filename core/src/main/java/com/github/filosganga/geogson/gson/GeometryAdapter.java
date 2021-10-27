package com.github.filosganga.geogson.gson;


import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.GeometryCollection;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.LinearRing;
import com.github.filosganga.geogson.model.MultiLineString;
import com.github.filosganga.geogson.model.MultiPoint;
import com.github.filosganga.geogson.model.MultiPolygon;
import com.github.filosganga.geogson.model.Point;
import com.github.filosganga.geogson.model.Polygon;
import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.github.filosganga.geogson.util.Preconditions;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GeometryAdapter extends TypeAdapter<Geometry> {
  public static final String GEOMETRY_TYPE_KEY = "type";
  public static final String GEOMETRY_GEOMETRIES_KEY = "geometries";
  public static final String GEOMETRY_COORDINATES_KEY = "coordinates";
  public static final String MSG_NOT_VALID_GEOMETRY_COLLECTION =
      "The given json is not a valid GeometryCollection";
  public static final String MSG_MISSING_KEY_COORDINATES =
      String.format("'%s' or '%s' are not specified", GEOMETRY_COORDINATES_KEY, GEOMETRY_GEOMETRIES_KEY);
  public static final String MSG_MISSING_KEY_GEOMETRY_TYPE =
      String.format("'%s' must be set and non-null", GEOMETRY_TYPE_KEY);
  public static final String MSG_MISSING_KEY_GEOMETRIES =
      String.format("'%s' is not specified", GEOMETRY_GEOMETRIES_KEY);
  public static final String MSG_WRONG_GEOMETRY_TYPE_TEMPLATE = "Cannot build a geometry for type: %s";
  public static final String MSG_GEOMETRIES_JSON_INVALID_TEMPLATE = "The json must be an array or null: %s";
  public static final String MSG_GEOMETRY_JSON_INVALID_TEMPLATE = "The given json is not a valid Geometry: %s";

  private final TypeAdapter<Geometry> geometryAdapter;
  private final TypeAdapter<Positions> positionsAdapter;

  public GeometryAdapter(Gson gson) {
    this.geometryAdapter = gson.getAdapter(Geometry.class);
    this.positionsAdapter = gson.getAdapter(Positions.class);
  }

  @Override
  public void write(JsonWriter out, Geometry value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.beginObject();

      out.name(GEOMETRY_TYPE_KEY).value(value.type().getValue());
      if (value.type() == Geometry.Type.GEOMETRY_COLLECTION) {
        out.name(GEOMETRY_GEOMETRIES_KEY);
        out.beginArray();
        GeometryCollection geometries = (GeometryCollection) value;
        for (Geometry<?> geometry : geometries.getGeometries()) {
          geometryAdapter.write(out, geometry);
        }
        out.endArray();
      } else {
        out.name(GEOMETRY_COORDINATES_KEY);
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
        if (GEOMETRY_TYPE_KEY.equals(name)) {
          type = Geometry.Type.forValue(in.nextString());
        } else if (GEOMETRY_COORDINATES_KEY.equals(name)) {
          positions = positionsAdapter.read(in);
        } else if (GEOMETRY_GEOMETRIES_KEY.equals(name)) {
          geometries = readGeometries(in);
        } else {
          in.skipValue();
        }
      }

      geometry = buildGeometry(type, positions, geometries);

      in.endObject();

    } else {
      throw new IllegalArgumentException(
          String.format(MSG_GEOMETRY_JSON_INVALID_TEMPLATE, in.peek()));
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
      throw new IllegalArgumentException(
          String.format(MSG_GEOMETRIES_JSON_INVALID_TEMPLATE, in.peek()));
    }

    return parsed;
  }

  private Geometry<?> parseGeometries(JsonReader in) throws IOException {

    Optional<Geometry<?>> parsed = Optional.empty();

    if (in.peek() != JsonToken.BEGIN_ARRAY) {
      throw new IllegalArgumentException(MSG_NOT_VALID_GEOMETRY_COLLECTION);
    }

    in.beginArray();
    if (in.peek() == JsonToken.BEGIN_OBJECT) {
      LinkedList<Geometry<?>> geometries = new LinkedList<>();
      while (in.hasNext()) {
        @SuppressWarnings("rawtypes")
        Geometry geometry = geometryAdapter.read(in);
        geometries.add(geometry);
      }
      parsed = Optional.of(GeometryCollection.of(geometries));
    }

    in.endArray();

    return parsed.orElse(null);
  }

  private Geometry<?> buildGeometry(final Geometry.Type type, final Positions positions, final Geometry<?> geometries) {
    Preconditions.checkArgument(type, Objects::nonNull, MSG_MISSING_KEY_GEOMETRY_TYPE);
    Preconditions.checkArgument(Objects.nonNull(positions) || Objects.nonNull(geometries),
        predicate -> predicate, MSG_MISSING_KEY_COORDINATES);
    switch (type) {
      case GEOMETRY_COLLECTION:
        return Preconditions.checkArgument(geometries, Objects::nonNull, MSG_MISSING_KEY_GEOMETRIES);
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
        throw new IllegalArgumentException(String.format(MSG_WRONG_GEOMETRY_TYPE_TEMPLATE, type));
    }
  }
}
