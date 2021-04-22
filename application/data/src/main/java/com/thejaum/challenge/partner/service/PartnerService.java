package com.thejaum.challenge.partner.service;

import com.thejaum.challenge.partner.business.CoordinateBusiness;
import com.thejaum.challenge.partner.business.PartnerBusiness;
import com.thejaum.challenge.partner.dto.PartnerCoordinatesDTO;
import com.thejaum.challenge.partner.model.Coordinate;
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

    public PartnerCoordinatesDTO registerNewPartner(PartnerCoordinatesDTO partnerCoordinatesDTO) throws IOException {
        log.info("Beginning to register a new Partner.");
        final Partner newPartner = partnerBusiness.createNewPartner(partnerCoordinatesDTO);
        log.info("Step one -> Partner {} registered.",newPartner.getId());
        //TODO Create a validation cause Address must be a POINT type
        final Coordinate address = coordinateBusiness.createNewCoordinate(partnerCoordinatesDTO.getAddress(), newPartner);
        log.info("Step two -> Addres {} registered.",address.getId());
        final Coordinate coverageArea = coordinateBusiness.createNewCoordinate(partnerCoordinatesDTO.getCoverageArea(), newPartner);
        log.info("Step three -> CoverageArea {} registered.",coverageArea.getId());
        return partnerCoordinatesDTO;
    }

    public PartnerCoordinatesDTO findPartnerAndCoordinatesByPartnerId(UUID partnerId) throws IOException {
        log.info("Searching a Partner of id {}",partnerId);
        final Partner partner = partnerBusiness.findPartnerById(partnerId);
        log.info("Partner found.");
        final List<Coordinate> coordinates = coordinateBusiness.findCoordinatesByPartnerId(partnerId);
        log.info("Coordinates size {}.",coordinates.size());
        final Coordinate address = coordinateBusiness.searchFirstCoordinateInListByType(coordinates, Geometry.TYPENAME_POINT);
        log.info("Addres {}.",address.getId());
        final Coordinate coverageArea = coordinateBusiness.searchFirstCoordinateInListByType(coordinates, Geometry.TYPENAME_MULTIPOLYGON);
        log.info("CoverageArea {}.",coverageArea.getId());
        return new PartnerCoordinatesDTO(
                partner,
                coordinateBusiness.GeometryToGeoJson(address.getGeometry()),
                coordinateBusiness.GeometryToGeoJson(coverageArea.getGeometry())
        );
    }

}
