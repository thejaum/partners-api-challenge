package business;

import com.thejaum.challenge.partner.business.ProximityEngineBusiness;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ProximityEngineTests {

    private final ProximityEngineBusiness proximityEngineBusiness;

    public ProximityEngineTests(ProximityEngineBusiness proximityEngineBusiness) {
        this.proximityEngineBusiness = proximityEngineBusiness;
    }

    static GeometryFactory gf = new GeometryFactory();

    @Test
    public void checkValidPointInsidePolygon(){
        Coordinate[] points = {
                new Coordinate(-46.664568185806274,-23.682219605636387),
                new Coordinate(-46.66401028633118,-23.68331021289563),
                new Coordinate(-46.6629159450531,-23.68289755176024),
                new Coordinate(-46.664568185806274,-23.682219605636387)};
        LineString line = gf.createLineString(points );

        double offset = 1000; //1km
        System.out.println(line);
        Polygon polygon = generatePoly(line,offset);
        System.out.println(polygon);
        final Point point = gf.createPoint(new Coordinate(-46.66374206542969, -23.68288772647923));
        System.out.println(point);
        boolean pointInsidePolygon = proximityEngineBusiness.isPointInsidePolygon(point, polygon);
        Assert.assertEquals(pointInsidePolygon,true);
    }

    private static Polygon generatePoly(LineString line, double offset) {

        Coordinate[] points = line.getCoordinates();

        ArrayList<Coordinate> soln = new ArrayList<>();
        //store initial points
        soln.addAll(Arrays.asList(points));
        // reverse the list
        ArrayUtils.reverse(points);
        // for each point move offset metres right
        for (Coordinate c:points) {
            soln.add(new Coordinate(c.x+offset, c.y));
        }
        // close the polygon
        soln.add(soln.get(0));
        // create polygon
        Polygon poly = gf.createPolygon(soln.toArray(new Coordinate[] {}));
        return poly;
    }
}
