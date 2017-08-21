package com.github.filosganga.geogson.jts;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import java.util.Iterator;

/**
 * A {@link Geometry} Iterable, to wrap the getGeometryN and getNumGeometries JTS methods.
 */
class JtsGeometryCollectionIterable implements Iterable<Geometry> {

    private final GeometryProvider src;

    private JtsGeometryCollectionIterable(GeometryProvider src) {
        this.src = src;
    }

    public static JtsGeometryCollectionIterable of(final GeometryCollection src) {
        return new JtsGeometryCollectionIterable(new GeometryProvider() {
            @Override
            public int getNumGeometries() {
                return src.getNumGeometries();
            }

            @Override
            public Geometry getGeometryN(int n) {
                return src.getGeometryN(n);
            }
        });
    }


    @Override
    public Iterator<Geometry> iterator() {
        return new JtsGeometryCollectionIterator(this.src);
    }

    private static class JtsGeometryCollectionIterator implements Iterator<Geometry> {

        private final GeometryProvider src;

        private int index = 0;

        private JtsGeometryCollectionIterator(GeometryProvider src) {
            this.src = src;
        }

        @Override
        public boolean hasNext() {
            return this.src.getNumGeometries() > this.index;
        }

        @Override
        public Geometry next() {
            return this.src.getGeometryN(this.index++);
        }

    }

    private interface GeometryProvider {

        int getNumGeometries();

        Geometry getGeometryN(int n);

    }
}
