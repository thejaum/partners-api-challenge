package com.thejaum.challenge.partner.distance;

public interface RouteEngine {

    boolean accept();

    Long findTravelTimeInSecondsBetweenTwoLocations(RouteDTO routeDTO);
}
