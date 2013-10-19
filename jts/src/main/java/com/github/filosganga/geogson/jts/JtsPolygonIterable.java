package com.github.filosganga.geogson.jts;

import com.google.common.collect.UnmodifiableIterator;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
class JtsPolygonIterable implements Iterable<Polygon> {

    private final PolygonProvider src;

    private JtsPolygonIterable(PolygonProvider src) {
        this.src = src;
    }

    public static JtsPolygonIterable of(final MultiPolygon src) {
        return new JtsPolygonIterable(new PolygonProvider() {
            @Override
            public int getNumPolygons() {
                return src.getNumGeometries();
            }

            @Override
            public Polygon getPolygonN(int n) {
                return (Polygon) src.getGeometryN(n);
            }
        });
    }


    @Override
    public UnmodifiableIterator<Polygon> iterator() {
        return new Iterator(src);
    }

    private static class Iterator extends UnmodifiableIterator<Polygon> {

        private final PolygonProvider src;

        private int index = 0;

        private Iterator(PolygonProvider src) {
            this.src = src;
        }

        @Override
        public boolean hasNext() {
            return src.getNumPolygons() > index;
        }

        @Override
        public Polygon next() {
            return src.getPolygonN(index++);

        }

    }

    private interface PolygonProvider {

        int getNumPolygons();

        Polygon getPolygonN(int n);

    }
}
