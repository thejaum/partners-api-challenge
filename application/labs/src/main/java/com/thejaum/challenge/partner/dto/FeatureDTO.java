package com.thejaum.challenge.partner.dto;

import lombok.*;
import org.geojson.GeoJsonObject;

import java.util.Map;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDTO {

    private String type;
    private Map<String,Object> properties;
    private GeoJsonObject geometry;

}
