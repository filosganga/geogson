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

package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.positions.Positions;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * The Gson TypeAdapterFactory responsible to serialize/de-serialize all the {@link Geometry}, {@link Feature}
 * and {@link FeatureCollection} instances.
 */
public final class GeometryAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (Geometry.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new GeometryAdapter(gson);
        } else if (Positions.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new PositionsAdapter();
        } else if (Feature.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new FeatureAdapter(gson);
        } else if (FeatureCollection.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new FeatureCollectionAdapter(gson);
        } else {
            return null;
        }
    }
}
