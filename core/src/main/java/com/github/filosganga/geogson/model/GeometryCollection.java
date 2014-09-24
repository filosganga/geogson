package com.github.filosganga.geogson.model;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class GeometryCollection extends AbstractGeometry<Positions> {

	protected GeometryCollection(MultiDimensionalPositions positions) {
		super(positions);
	}

	public static GeometryCollection of(Iterable<Geometry<?>> geometries) {
		System.out.println(GeometryCollection.class.getSimpleName() + "--> of: " + geometries);
		
        return new GeometryCollection(
                new MultiDimensionalPositions(transform(geometries, positionsFn(MultiDimensionalPositions.class))));
    }

    @Override
    public Type type() {
        return Type.GEOMETRY_COLLECTION;
    }
 
    public static Function positionsFn(Class positionsClass) {
        return new Function() {
            @Override
            public Object apply(Object input) {
            	Geometry<?> g = (Geometry) input;
                return g.positions();
            }
        };
    }
    
   /* public static <P extends Positions> Function<AbstractGeometry<P>, P> positionsFn(Class<P> positionsClass) {
        return new Function<AbstractGeometry<P>, P>() {
            @Override
            public P apply(AbstractGeometry<P> input) {
                return input.positions();
            }
        };
    }*/

}
