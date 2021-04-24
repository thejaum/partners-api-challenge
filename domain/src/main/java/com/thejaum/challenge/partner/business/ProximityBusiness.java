package com.thejaum.challenge.partner.business;

import com.thejaum.challenge.partner.model.PartnerLocation;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProximityBusiness {

    public boolean isPointInsidePolygon(Point point, Polygon polygon){
        return polygon.contains(point);
    };

    public boolean isPointInsideMultiPolygon(Point point,MultiPolygon multiPolygon){
        return multiPolygon.contains(point);
    };

    public List<PartnerLocation> extractPartnersLocationsThatCoverAnPoint(List<PartnerLocation> partnerLocations,Point location ){
        return partnerLocations.stream().map(partnerLocation -> {
            if (this.isPointInsideMultiPolygon(location, partnerLocation.getGeometry().getFactory().createMultiPolygon())) {
                return partnerLocation;
            }
            return null;
        }).collect(Collectors.toList());
    }
}
