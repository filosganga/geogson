package com.github.filosganga.geogson.codec;

import static com.google.common.collect.Maps.newConcurrentMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.reflect.TypeToken;

/**
 * A registry of codecs used as façade to convert to and from different geometry systems.
 */
public class CodecRegistry<T, S extends Geometry<?>> implements Codec<T, S> {

    private final Map<Type, Codec<T,S>> codecsByS = newConcurrentMap();
    private final Map<Type, Codec<T,S>> codecsByT = newConcurrentMap();

    public CodecRegistry() {
        this(new ArrayList<Codec<? extends T, ? extends S>>());
    }

    public CodecRegistry(Iterable<Codec<? extends T, ? extends S>> codecs ) {
        for(Codec<? extends T, ? extends S> codec : codecs) {
            addCodec(codec);
        }
    }

    @SuppressWarnings("unchecked")
    public void addCodec(Codec<? extends T, ? extends S> codec) {

        ParameterizedType type = (ParameterizedType) TypeToken.of(codec.getClass()).getSupertype(Codec.class).getType();

        Type t = type.getActualTypeArguments()[0];
        Type s = type.getActualTypeArguments()[1];

        codecsByS.put(s, (Codec<T, S>) codec);
        codecsByT.put(t, (Codec<T, S>) codec);
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
