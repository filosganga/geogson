package org.filippodeluca.geogson.model;

import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class LinearGeometry extends Geometry {

    protected final ImmutableList<Position> coordinates;

    public LinearGeometry(ImmutableList<Position> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public LinearPositions getPositions() {
        return new LinearPositions(coordinates);
    }

    public ImmutableList<Position> getCoordinates() {
        return coordinates;
    }

    public int getSize() {
        return coordinates.size();
    }
}
