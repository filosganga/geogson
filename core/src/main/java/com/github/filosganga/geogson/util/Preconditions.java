package com.github.filosganga.geogson.util;

import java.util.Objects;
import java.util.function.Function;

public class Preconditions {

    public static <T> T checkArgument(T argument, Function<T, Boolean> predicate, Object errorMessage) {
        if (!predicate.apply(argument)) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        } else {
            return argument;
        }
    }

    public static <T> T checkNotNull(T argument) {
        return checkNotNull(argument,"Argument should be not null");
    }

    public static <T> T checkNotNull(T argument, Object errorMessage) {
        return checkArgument(argument, Objects::nonNull, errorMessage);
    }
}
