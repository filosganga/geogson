package com.github.filosganga.geogson.jts;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Polygon;

import java.util.Iterator;

/**
 * A {@link LineString} Iterable, to wrap the getLineStringN and getNumLineStrings JTS methods.
 */
class JtsLineStringIterable implements Iterable<LineString> {

    private final LineStringProvider src;

    private JtsLineStringIterable(LineStringProvider src) {
        this.src = src;
    }

    public static JtsLineStringIterable of(final MultiLineString src) {
        return new JtsLineStringIterable(new LineStringProvider() {
            @Override
            public int getNumLineStrings() {
                return src.getNumGeometries();
            }

            @Override
            public LineString getLineStringN(int n) {
                return (LineString) src.getGeometryN(n);
            }
        });
    }

    public static JtsLineStringIterable forHolesOf(final Polygon src) {
        return new JtsLineStringIterable(new LineStringProvider() {
            @Override
            public int getNumLineStrings() {
                return src.getNumInteriorRing();
            }

            @Override
            public LineString getLineStringN(int n) {
                return src.getInteriorRingN(n);
            }
        });
    }


    @Override
    public Iterator<LineString> iterator() {
        return new JtsLineStringIterator(src);
    }

    private static class JtsLineStringIterator implements Iterator<LineString> {

        private final LineStringProvider src;

        private int index = 0;

        private JtsLineStringIterator(LineStringProvider src) {
            this.src = src;
        }

        @Override
        public boolean hasNext() {
            return src.getNumLineStrings() > index;
        }

        @Override
        public LineString next() {
            return src.getLineStringN(index++);

        }

    }

    private interface LineStringProvider {

        int getNumLineStrings();

        LineString getLineStringN(int n);

    }
}
