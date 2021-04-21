package com.thejaum.challenge.partner.dto;

import lombok.*;
import org.geojson.GeoJsonObject;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {

    private long id;

    private String tradingName;

    private String ownerName;

    private String document;

    //coverageArea
    private GeoJsonObject coverageArea;
    //address
    private GeoJsonObject address;

}
