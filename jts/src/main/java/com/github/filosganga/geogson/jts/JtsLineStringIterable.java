package com.github.filosganga.geogson.jts;

import com.google.common.collect.UnmodifiableIterator;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

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
    public UnmodifiableIterator<LineString> iterator() {
        return new Iterator(src);
    }

    private static class Iterator extends UnmodifiableIterator<LineString> {

        private final LineStringProvider src;

        private int index = 0;

        private Iterator(LineStringProvider src) {
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
