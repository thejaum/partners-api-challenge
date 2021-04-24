package business;

import com.thejaum.challenge.partner.business.ProximityBusiness;
import configuration.GeometryBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;

public class ProximityEngineTests extends GeometryBaseTest {

    @Autowired
    private ProximityBusiness proximityBusiness;

    @Test
    public void checkPointInsidePolygon(){
        Coordinate[] coords = {
                new Coordinate(-46.664568185806274,-23.682219605636387),
                new Coordinate(-46.66401028633118,-23.68331021289563),
                new Coordinate(-46.6629159450531,-23.68289755176024),
                new Coordinate(-46.664568185806274,-23.682219605636387)};
        LinearRing lines = this.createAnLinearRingFromCoordinates(coords);
        Polygon polygon = this.createAnPolygonFromLinearRing(lines);
        Point point = this.createAnPointFromCoordinate(new Coordinate(-46.66374206542969, -23.68288772647923));
        boolean pointInsidePolygon = proximityBusiness.isPointInsidePolygon(point, polygon);
        Assert.assertEquals(pointInsidePolygon,true);
    }

    @Test
    public void checkPointOutsidePolygon(){
        Coordinate[] coords = {
                new Coordinate(-54.57844734191894,-19.38912923598742),
                new Coordinate(-54.58179473876953,-19.392448679313784),
                new Coordinate(-54.576215744018555,-19.393501171599986),
                new Coordinate(-54.57844734191894,-19.38912923598742)};
        LinearRing lines = this.createAnLinearRingFromCoordinates(coords);
        Polygon polygon = this.createAnPolygonFromLinearRing(lines);
        Point point = this.createAnPointFromCoordinate(new Coordinate(-54.582438468933105, -19.392327237458208));
        boolean pointInsidePolygon = proximityBusiness.isPointInsidePolygon(point, polygon);
        Assert.assertEquals(pointInsidePolygon,false);
    }


}
