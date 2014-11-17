package com.github.filosganga.geogson.codec;

import com.github.filosganga.geogson.model.Geometry;

/**
 * It converts to and from {@link Geometry}. It is mainly used to support other Geometry model like JTS.
 */
public interface Codec<S, T extends Geometry<?>> {

    /**
     * Converts the given S instance to T.
     *
     * @param src a S instance to convert.
     * @return a T instance.
     */
    T toGeometry(S src);

    /**
     * Converts the given T instance to S.
     *
     * @param src a T instance to convert.
     * @return a S instance.
     */
    S fromGeometry(T src);
}
