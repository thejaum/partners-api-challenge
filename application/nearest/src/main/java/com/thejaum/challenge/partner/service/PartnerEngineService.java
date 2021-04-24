package com.thejaum.challenge.partner.service;

import com.thejaum.challenge.partner.business.CoordinateBusiness;
import com.thejaum.challenge.partner.business.ProximityBusiness;
import com.thejaum.challenge.partner.model.PartnerLocation;
import com.thejaum.challenge.partner.util.GeometryHelper;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PartnerEngineService {

    private CoordinateBusiness coordinateBusiness;
    private ProximityBusiness proximityBusiness;
    private GeometryHelper geometryHelper;

    public PartnerEngineService(CoordinateBusiness coordinateBusiness, ProximityBusiness proximityBusiness, GeometryHelper geometryHelper) {
        this.coordinateBusiness = coordinateBusiness;
        this.proximityBusiness = proximityBusiness;
        this.geometryHelper = geometryHelper;
    }

    public List<PartnerLocation> findNearestPartner(Double lng, Double lat){
        log.info("Searching Partners for long {}, lat {} covered by pre-defined range.",lng,lat);
        Point location = geometryHelper.createAnPointFromCoordinate(new Coordinate(lng, lat));

        List<PartnerLocation> partnerLocations = coordinateBusiness.findNearestCoordinatesFromAnPointWithRange(lng,lat);
        log.info("Partners found in range -> "+partnerLocations.size());

        List<PartnerLocation> partnerCoverageAreas = partnerLocations.stream().map(partnerLocation -> {
            return coordinateBusiness.findCoverageAreaByPartnerId(partnerLocation.getPartner().getId());
        }).collect(Collectors.toList());
        List<PartnerLocation> partnerCoverageAreaCover = proximityBusiness.extractPartnersLocationsThatCoverAnPoint(partnerCoverageAreas, location);
        log.info("Partners where CoverageArea cover -> "+partnerCoverageAreaCover.size());

        return null;
    }
}
