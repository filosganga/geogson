package com.github.filosganga.geogson.jts;

import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import java.util.Iterator;

/**
 * A {@link Polygon} Iterable, to wrap the getPolygonN and getNumPolygons JTS methods.
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
    public Iterator<Polygon> iterator() {
        return new JtsPolygonIterator(src);
    }

    private static class JtsPolygonIterator implements Iterator<Polygon> {

        private final PolygonProvider src;

        private int index = 0;

        private JtsPolygonIterator(PolygonProvider src) {
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
