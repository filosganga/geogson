package com.github.filosganga.geogson.jts;

import com.google.common.collect.UnmodifiableIterator;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

public class JtsGeometryCollectionIterable implements Iterable<Geometry> {

	private final GeometryProvider src;

	 private JtsGeometryCollectionIterable(GeometryProvider src) {
		 System.out.println(this.getClass().getSimpleName() + "--> constructor with src and geometries: " + src.getNumGeometries());
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
	                return (Geometry) src.getGeometryN(n);
	            }
	        });
	    }


	@Override
	public UnmodifiableIterator<Geometry> iterator() {
		return new Iterator(src);
	}

	private static interface GeometryProvider {

		int getNumGeometries();

		Geometry getGeometryN(int n);

	}
	
	private static class Iterator extends UnmodifiableIterator<Geometry> {

        private final GeometryProvider src;

        private int index = 0;

        private Iterator(GeometryProvider src) {
            this.src = src;
        }

        @Override
        public boolean hasNext() {
            return src.getNumGeometries() > index;
        }

        @Override
        public Geometry next() {
        	System.out.println(this.getClass().getSimpleName() + "--> next");
            return src.getGeometryN(index++);

        }

    }

}
