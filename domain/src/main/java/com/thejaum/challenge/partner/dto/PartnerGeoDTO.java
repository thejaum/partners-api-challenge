package com.thejaum.challenge.partner.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.geojson.GeoJsonObject;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PartnerGeoDTO {

    @JsonIgnore
    private UUID id;

    @NotEmpty(message = "Trading name cannot be empty or null.")
    private String tradingName;

    @NotEmpty(message = "Owner name cannot be empty or null.")
    private String ownerName;

    @NotEmpty(message = "Document cannot be empty or null.")
    private String document;

    @NotNull(message = "Address cannot be null.")
    private GeoJsonObject address;

    @NotNull(message = "Coverage Area cannot be null.")
    private GeoJsonObject coverageArea;

    @JsonProperty
    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public void setId(UUID id) {
        this.id = id;
    }
}
