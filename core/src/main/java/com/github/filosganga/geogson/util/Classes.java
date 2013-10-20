package com.github.filosganga.geogson.util;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public final class Classes {

    private Classes() {
        // Do not instantiate
    }

    public static  <S, T> T cast(S src) {
        return (T) src;
    }

    public static <S, T> T castTp(S src, Class<T> toClass) {
        return (T) src;
    }

}
