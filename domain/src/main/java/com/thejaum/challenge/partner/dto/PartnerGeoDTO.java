package com.thejaum.challenge.partner.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thejaum.challenge.partner.model.Partner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.geojson.GeoJsonObject;

import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PartnerGeoDTO {

    @JsonIgnore
    private UUID id;

    private String tradingName;

    private String ownerName;

    private String document;

    private GeoJsonObject address;

    private GeoJsonObject coverageArea;
}
