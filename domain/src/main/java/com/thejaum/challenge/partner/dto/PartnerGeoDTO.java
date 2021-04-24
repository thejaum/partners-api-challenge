package com.thejaum.challenge.partner.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.geojson.GeoJsonObject;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PartnerGeoDTO {

    @JsonIgnore
    private UUID id;

    @NotEmpty(message = "Trading name can not be empty or null.")
    private String tradingName;

    @NotEmpty(message = "Owner name can not be empty or null.")
    private String ownerName;

    @NotEmpty(message = "Document can not be empty or null.")
    private String document;

    private GeoJsonObject address;

    private GeoJsonObject coverageArea;
}
