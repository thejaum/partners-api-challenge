package com.thejaum.challenge.partner.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.geojson.GeoJsonObject;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {

    @JsonIgnore
    private UUID id;

    private String tradingName;

    private String ownerName;

    private String document;

    private GeoJsonObject coverageArea;

    private GeoJsonObject address;

}
