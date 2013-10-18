package org.filippodeluca.geogson.model;

import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class LinearGeometry extends Geometry {

    protected final LinearPositions coordinates;

    public LinearGeometry(LinearPositions coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public LinearPositions getPositions() {
        return coordinates;
    }

    public ImmutableList<Position> getCoordinates() {
        return coordinates.getPositions();
    }

    public int getSize() {
        return coordinates.getPositions().size();
    }
}
