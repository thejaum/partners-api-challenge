package com.thejaum.challenge.partner.util;

import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

@Component
public class GeometryHelper {

    static GeometryFactory geometryFactory = new GeometryFactory();

    public LineString createAnLineFromCoordinates(Coordinate[] coords){
        LineString line = geometryFactory.createLineString(coords );
        return line;
    }
    public LinearRing createAnLinearRingFromCoordinates(Coordinate[] coords){
        LinearRing linearRing = geometryFactory.createLinearRing(coords);
        return linearRing;
    }
    public Polygon createAnPolygonFromLinearRing(LinearRing ring) {
        Polygon polygon = geometryFactory.createPolygon(ring);
        return polygon;
    }
    public Point createAnPointFromCoordinate(Coordinate coordinate){
        Point point = geometryFactory.createPoint(coordinate);
        return point;
    }

}
