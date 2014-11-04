package com.github.filosganga.geogson.jts;

import com.google.common.collect.UnmodifiableIterator;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * @author lurajon jone@ecc.no
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
    public UnmodifiableIterator<Geometry> iterator() {
        return new Iterator(this.src);
    }

    private static class Iterator extends UnmodifiableIterator<Geometry> {

        private final GeometryProvider src;

        private int index = 0;

        private Iterator(GeometryProvider src) {
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
