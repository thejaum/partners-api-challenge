package com.thejaum.challenge.partner.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;

@Component
public class GeometryHelper {

    private ObjectMapper objectMapper;

    public GeometryHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
    public Geometry geoJsonToGeoToolsGeometry(GeoJsonObject geoJsonObject) throws IOException {
        GeometryJSON gjson = new GeometryJSON();
        String json = objectMapper.writeValueAsString(geoJsonObject);
        return gjson.read(json);
    }

    public GeoJsonObject geoToolsGeometryToGeoJson(Geometry geometry) throws IOException {
        GeoJsonObject geoJsonObject=null;
        GeometryJSON gjson = new GeometryJSON();
        try (StringWriter stWriterF = new StringWriter()){
            gjson.write(geometry,stWriterF);
            geoJsonObject = this.objectMapper.readValue(stWriterF.toString(), GeoJsonObject.class);
        }
        return geoJsonObject;
    }

    public boolean isPointInsidePolygon(Point point, Polygon polygon){
        return polygon.contains(point);
    };

    public boolean isPointInsideMultiPolygon(Point point,MultiPolygon multiPolygon){
        MultiLineString boundary = (MultiLineString) multiPolygon.getBoundary();
        int count=0;
        int dimension = boundary.getNumGeometries();
        if(dimension>0) {
            do {
                Polygon polygon = createAnPolygonFromLinearRing((LinearRing) boundary.getGeometryN(count));
                if (isPointInsidePolygon(point, polygon))
                    return true;
                count++;
            } while (count < dimension);
        }
        return false;
    };

}
