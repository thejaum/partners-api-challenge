package com.thejaum.challenge.partner.util;

import com.thejaum.challenge.partner.configuration.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;

public class GeometryHelperTests extends BaseTest {

    @Autowired
    private GeometryHelper geometryHelper;

    @Test
    public void checkPointInsidePolygon(){
        Coordinate[] coords = {
                new Coordinate(-46.664568185806274,-23.682219605636387),
                new Coordinate(-46.66401028633118,-23.68331021289563),
                new Coordinate(-46.6629159450531,-23.68289755176024),
                new Coordinate(-46.664568185806274,-23.682219605636387)};
        LinearRing lines = geometryHelper.createAnLinearRingFromCoordinates(coords);
        Polygon polygon = geometryHelper.createAnPolygonFromLinearRing(lines);
        Point point = geometryHelper.createAnPointFromCoordinate(new Coordinate(-46.66374206542969, -23.68288772647923));
        boolean pointInsidePolygon = geometryHelper.isPointInsidePolygon(point, polygon);
        Assert.assertEquals(pointInsidePolygon,true);
    }

    @Test
    public void checkPointOutsidePolygon(){
        Coordinate[] coords = {
                new Coordinate(-54.57844734191894,-19.38912923598742),
                new Coordinate(-54.58179473876953,-19.392448679313784),
                new Coordinate(-54.576215744018555,-19.393501171599986),
                new Coordinate(-54.57844734191894,-19.38912923598742)};
        LinearRing lines = geometryHelper.createAnLinearRingFromCoordinates(coords);
        Polygon polygon = geometryHelper.createAnPolygonFromLinearRing(lines);
        Point point = geometryHelper.createAnPointFromCoordinate(new Coordinate(-54.582438468933105, -19.392327237458208));
        boolean pointInsidePolygon = geometryHelper.isPointInsidePolygon(point, polygon);
        Assert.assertEquals(pointInsidePolygon,false);
    }

    @Test
    public void checkPointInsideMultiPolygon(){
        Coordinate[] coordsOne = {
                new Coordinate(-49.03279781341553,-22.330129427571304),
                new Coordinate(-49.03406381607056,-22.33326546517202),
                new Coordinate(-49.03002977371216,-22.333662426929326),
                new Coordinate(-49.03279781341553,-22.330129427571304)};
        LinearRing linesOne = geometryHelper.createAnLinearRingFromCoordinates(coordsOne);
        Polygon polygonOne = geometryHelper.createAnPolygonFromLinearRing(linesOne);
        Coordinate[] coordsTwo = {
                new Coordinate(-49.03530836105346,-22.335488436460146),
                new Coordinate(-49.03453588485718,-22.328561382331014),
                new Coordinate(-49.03816223144531,-22.332015028251725),
                new Coordinate(-49.03530836105346,-22.335488436460146)};
        LinearRing linesTwo = geometryHelper.createAnLinearRingFromCoordinates(coordsTwo);
        Polygon polygonTwo = geometryHelper.createAnPolygonFromLinearRing(linesTwo);
        Polygon[] polygons = new Polygon[]{polygonOne,polygonTwo};
        MultiPolygon multipolygon = new MultiPolygon(polygons, geometryHelper.geometryFactory);
        Point point = geometryHelper.createAnPointFromCoordinate(new Coordinate(-49.03548002243041, -22.332411993567945));
        boolean pointInsideMultiPolygon = geometryHelper.isPointInsideMultiPolygon(point, multipolygon);
        Assert.assertEquals(pointInsideMultiPolygon,true);
    }

    @Test
    public void checkPointOutsideMultiPolygon(){
        Coordinate[] coordsOne = {
                new Coordinate(-49.03279781341553,-22.330129427571304),
                new Coordinate(-49.03406381607056,-22.33326546517202),
                new Coordinate(-49.03002977371216,-22.333662426929326),
                new Coordinate(-49.03279781341553,-22.330129427571304)};
        LinearRing linesOne = geometryHelper.createAnLinearRingFromCoordinates(coordsOne);
        Polygon polygonOne = geometryHelper.createAnPolygonFromLinearRing(linesOne);
        Coordinate[] coordsTwo = {
                new Coordinate(-49.03530836105346,-22.335488436460146),
                new Coordinate(-49.03453588485718,-22.328561382331014),
                new Coordinate(-49.03816223144531,-22.332015028251725),
                new Coordinate(-49.03530836105346,-22.335488436460146)};
        LinearRing linesTwo = geometryHelper.createAnLinearRingFromCoordinates(coordsTwo);
        Polygon polygonTwo = geometryHelper.createAnPolygonFromLinearRing(linesTwo);
        Polygon[] polygons = new Polygon[]{polygonOne,polygonTwo};
        MultiPolygon multipolygon = new MultiPolygon(polygons, geometryHelper.geometryFactory);
        Point point = geometryHelper.createAnPointFromCoordinate(new Coordinate(-49.04157400131225, -22.335825848647627));
        boolean pointInsideMultiPolygon = geometryHelper.isPointInsideMultiPolygon(point, multipolygon);
        Assert.assertEquals(pointInsideMultiPolygon,false);
    }



}
