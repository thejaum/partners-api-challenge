package com.thejaum.challenge.partner.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thejaum.challenge.partner.model.Partner;
import lombok.*;
import org.geojson.GeoJsonObject;

import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PartnerCoordinatesDTO extends PartnerDTO{

    private GeoJsonObject coverageArea;

    private GeoJsonObject address;

    public PartnerCoordinatesDTO(Partner partner, GeoJsonObject coverageArea, GeoJsonObject address) {
        super(partner.getId(), partner.getTradingName(), partner.getOwnerName(), partner.getDocument());
        this.coverageArea = coverageArea;
        this.address = address;
    }
}
