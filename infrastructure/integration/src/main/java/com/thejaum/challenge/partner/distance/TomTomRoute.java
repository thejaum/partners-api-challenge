package com.thejaum.challenge.partner.distance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Slf4j
@Component
public class TomTomRoute implements RouteEngine {

    private final String BASE_URL="https://api.tomtom.com/routing/1/calculateRoute/";

    @Value("${tomtom.key:})")
    private String key;

    @Override
    public boolean accept() {
        return null != key;
    }

    @Override
    public Long findTravelTimeInSecondsBetweenTwoLocations(RouteDTO routeDTO) {
        log.info("findTravelTimeInSecondsBetweenTwoLocations -> {}",routeDTO.toString());
        RestTemplate restTemplate = new RestTemplate();
        String composedUri = BASE_URL+routeDTO.getOriginLat()+","+routeDTO.getOriginLng()+":"+routeDTO.getDestinationLat()+","+routeDTO.getDestinationLng()
                +"/json?key="+key+"&travelMode=car&traffic=true";
        try {
            //TODO Get SDK from TomTom with ResonseObject for this serialization.
            Object response = restTemplate.getForObject(composedUri, Object.class);
            Long seconds = extractTomTomResponse(response);
            log.info("Time -> {}",seconds);
            return seconds;
        }catch (RestClientException rce){
            log.error("Problem with integration.",rce.getLocalizedMessage());
            return -1L;
        }
    }

    private Long extractTomTomResponse(Object response){
        LinkedHashMap map = (LinkedHashMap)response;
        ArrayList routes = (ArrayList) map.getOrDefault("routes", "");
        LinkedHashMap newmap = (LinkedHashMap) routes.get(0);
        LinkedHashMap summary = (LinkedHashMap) newmap.getOrDefault("summary", "");
        Long seconds = Long.valueOf((Integer)summary.getOrDefault("travelTimeInSeconds",""));
        return seconds;
    }
}
