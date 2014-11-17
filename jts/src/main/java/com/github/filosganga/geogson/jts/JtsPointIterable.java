package com.github.filosganga.geogson.jts;

import com.google.common.collect.UnmodifiableIterator;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/**
 * A {@link Point} Iterable, to wrap the getPointN and getNumPoints JTS methods.
 */
class JtsPointIterable implements Iterable<Point> {

    private final PointProvider src;

    private JtsPointIterable(PointProvider src) {
        this.src = src;
    }

    public static JtsPointIterable of(final LineString src) {
        return new JtsPointIterable(new PointProvider() {
            @Override
            public int getNumPoints() {
                return src.getNumPoints();
            }

            @Override
            public Point getPointN(int n) {
                return src.getPointN(n);
            }
        });
    }

    public static JtsPointIterable of(final MultiPoint src) {
        return new JtsPointIterable(new PointProvider() {
            @Override
            public int getNumPoints() {
                return src.getNumGeometries();
            }

            @Override
            public Point getPointN(int n) {
                return (Point) src.getGeometryN(n);
            }
        });
    }

    @Override
    public UnmodifiableIterator<Point> iterator() {
        return new JtsPointIterator(src);
    }

    private class JtsPointIterator extends UnmodifiableIterator<Point> {

        private final PointProvider src;

        private int index = 0;

        private JtsPointIterator(PointProvider src) {
            this.src = src;
        }

        @Override
        public boolean hasNext() {
            return src.getNumPoints() > index;
        }

        @Override
        public Point next() {
            return src.getPointN(index++);

        }

    }

    private interface PointProvider {

        int getNumPoints();

        Point getPointN(int n);

    }
}
