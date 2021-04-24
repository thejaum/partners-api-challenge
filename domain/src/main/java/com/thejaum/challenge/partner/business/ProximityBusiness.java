package com.thejaum.challenge.partner.business;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

@Component
public class ProximityBusiness {

    public boolean isPointInsidePolygon(Point point, Polygon polygon){
        return polygon.contains(point);
    };
}
