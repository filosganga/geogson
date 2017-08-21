package com.github.filosganga.geogson.gson.utils;

import com.github.filosganga.geogson.model.Coordinates;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JsonUtils {


    public static String givenMultiDimensionalPositionsJson(Iterable<Iterable<? extends Iterable<Coordinates>>> areas) {

        return StreamSupport.stream(areas.spliterator(), false)
                .map(JsonUtils::givenAreaPositionsJson)
                .collect(Collectors.joining(",", "[", "]"));
    }


    public static String givenAreaPositionsJson(Iterable<? extends Iterable<Coordinates>> lines) {

        return StreamSupport.stream(lines.spliterator(), false)
                .map(JsonUtils::givenLinearPositionsJson)
                .collect(Collectors.joining(",", "[", "]"));
    }

    public static String givenLinearPositionsJson(Iterable<Coordinates> coordinates) {

        return StreamSupport.stream(coordinates.spliterator(), false)
                .map(c -> givenSinglePositionJson(c.getLon(), c.getLat()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    public static String givenSinglePositionJson(double lon, double lat) {

        return "[" + lon + "," + lat + "]";
    }

    private JsonUtils() {

    }
}
