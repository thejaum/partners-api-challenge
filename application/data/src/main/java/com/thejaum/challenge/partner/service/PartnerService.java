package com.thejaum.challenge.partner.service;

import com.thejaum.challenge.partner.business.CoordinateBusiness;
import com.thejaum.challenge.partner.business.PartnerBusiness;
import com.thejaum.challenge.partner.dto.PartnerLocationsDTO;
import com.thejaum.challenge.partner.model.PartnerLocation;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.transformer.PartnerTransformer;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PartnerService {

    private PartnerBusiness partnerBusiness;
    private CoordinateBusiness coordinateBusiness;
    private PartnerTransformer partnerTransformer;

    public PartnerService(PartnerBusiness partnerBusiness, CoordinateBusiness coordinateBusiness) {
        this.partnerBusiness = partnerBusiness;
        this.coordinateBusiness = coordinateBusiness;
    }

    public PartnerLocationsDTO registerNewPartner(PartnerLocationsDTO partnerLocationsDTO) throws IOException {
        log.info("Beginning to register a new Partner.");
        final Partner newPartner = partnerBusiness.createNewPartner(partnerLocationsDTO);
        log.info("Step one -> Partner {} registered.",newPartner.getId());
        //TODO Create a validation cause Address must be a POINT type
        final PartnerLocation address = coordinateBusiness.createNewCoordinate(partnerLocationsDTO.getAddress(), newPartner);
        log.info("Step two -> Addres {} registered.",address.getId());
        final PartnerLocation coverageArea = coordinateBusiness.createNewCoordinate(partnerLocationsDTO.getCoverageArea(), newPartner);
        log.info("Step three -> CoverageArea {} registered.",coverageArea.getId());
        return partnerLocationsDTO;
    }

    public PartnerLocationsDTO findPartnerAndCoordinatesByPartnerId(UUID partnerId) throws IOException {
        log.info("Searching a Partner of id {}",partnerId);
        final Partner partner = partnerBusiness.findPartnerById(partnerId);
        log.info("Partner found.");
        final List<PartnerLocation> partnerLocations = coordinateBusiness.findCoordinatesByPartnerId(partnerId);
        log.info("Coordinates size {}.", partnerLocations.size());
        final PartnerLocation address = coordinateBusiness.searchFirstCoordinateInListByType(partnerLocations, Geometry.TYPENAME_POINT);
        log.info("Addres {}.",address.getId());
        final PartnerLocation coverageArea = coordinateBusiness.searchFirstCoordinateInListByType(partnerLocations, Geometry.TYPENAME_MULTIPOLYGON);
        log.info("CoverageArea {}.",coverageArea.getId());
        return new PartnerLocationsDTO(
                partner,
                coordinateBusiness.GeometryToGeoJson(coverageArea.getGeometry()),
                coordinateBusiness.GeometryToGeoJson(address.getGeometry())
        );
    }

}
