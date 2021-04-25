package com.thejaum.challenge.partner.transformer;

import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.dto.PartnerGeoDTO;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.util.GeometryHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PartnerTransformer extends BaseTransformer<PartnerDTO,Partner>{

    private GeometryHelper geometryHelper;

    public PartnerTransformer(GeometryHelper geometryHelper) {
        this.geometryHelper = geometryHelper;
    }

    public PartnerDTO toDtoMapperFromGeo(PartnerGeoDTO partnerGeoDTO) throws IOException {
        PartnerDTO partnerDTO = getModelMapper().map(partnerGeoDTO, PartnerDTO.class);
        partnerDTO.setAddress(geometryHelper.geoJsonToGeoToolsGeometry(partnerGeoDTO.getAddress()));
        partnerDTO.setCoverageArea(geometryHelper.geoJsonToGeoToolsGeometry(partnerGeoDTO.getCoverageArea()));
        return partnerDTO;
    }

    public PartnerGeoDTO toGeoDtoMapperFromDto(PartnerDTO partnerDTO) throws IOException {
        PartnerGeoDTO partnerGeoDTO = new PartnerGeoDTO();
        partnerGeoDTO.setId(partnerDTO.getId());
        partnerGeoDTO.setTradingName(partnerDTO.getTradingName());
        partnerGeoDTO.setOwnerName(partnerDTO.getOwnerName());
        partnerGeoDTO.setDocument(partnerDTO.getDocument());
        partnerGeoDTO.setAddress(geometryHelper.geoToolsGeometryToGeoJson(partnerDTO.getAddress()));
        partnerGeoDTO.setCoverageArea(geometryHelper.geoToolsGeometryToGeoJson(partnerDTO.getCoverageArea()));
        return partnerGeoDTO;
    }

    public PartnerGeoDTO toGeoDtoMapperFromEntity(Partner partner) throws IOException {
        PartnerGeoDTO partnerGeoDTO = new PartnerGeoDTO();
        partnerGeoDTO.setId(partner.getId());
        partnerGeoDTO.setTradingName(partner.getTradingName());
        partnerGeoDTO.setOwnerName(partner.getOwnerName());
        partnerGeoDTO.setDocument(partner.getDocument());
        partnerGeoDTO.setAddress(geometryHelper.geoToolsGeometryToGeoJson(partner.getAddress()));
        partnerGeoDTO.setCoverageArea(geometryHelper.geoToolsGeometryToGeoJson(partner.getCoverageArea()));
        return partnerGeoDTO;
    }
}
