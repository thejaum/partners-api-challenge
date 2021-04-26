package com.thejaum.challenge.partner.distance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RouteGateway {
    STANDARD(0, "Standard"),
    ROUTE(1, "Route");

    @JsonProperty(value = "id")
    private final Integer id;

    @JsonProperty(value = "value")
    private final String value;

    RouteGateway(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static RouteGateway getById(Integer id) {

        return Arrays.stream(RouteGateway.values())
                .filter(routeGateway -> routeGateway.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static RouteGateway getByValue(String value) {

        return Arrays.stream(RouteGateway.values())
                .filter(routeGateway -> routeGateway.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new InvalidRouteEngineException("Invalid Route Engine."));
    }
}
