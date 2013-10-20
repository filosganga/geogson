package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Maps.newConcurrentMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.reflect.TypeToken;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class CodecRegistry<T, S extends Geometry<?>> implements JtsCodec<T, S> {

    private Map<Type, JtsCodec<T,S>> codecsByS = newConcurrentMap();
    private Map<Type, JtsCodec<T,S>> codecsByT = newConcurrentMap();

    public void addCodec(JtsCodec<? extends T, ? extends S> codec) {

        ParameterizedType type = (ParameterizedType) TypeToken.of(codec.getClass()).getSupertype(JtsCodec.class).getType();

        Type t = type.getActualTypeArguments()[0];
        Type s = type.getActualTypeArguments()[1];

        codecsByS.put(s, (JtsCodec<T, S>) codec);
        codecsByT.put(t, (JtsCodec<T, S>) codec);
    }

    @Override
    public S toGeometry(T src) {
        return codecsByT.get(src.getClass()).toGeometry(src);
    }

    @Override
    public T fromGeometry(S src) {
        return codecsByS.get(src.getClass()).fromGeometry(src);
    }
}
