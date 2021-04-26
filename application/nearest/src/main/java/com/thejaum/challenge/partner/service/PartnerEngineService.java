package com.thejaum.challenge.partner.service;

import com.thejaum.challenge.partner.business.PartnerBusiness;
import com.thejaum.challenge.partner.dto.PartnerGeoDTO;
import com.thejaum.challenge.partner.exception.NotFoundException;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.transformer.PartnerTransformer;
import com.thejaum.challenge.partner.util.GeometryHelper;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PartnerEngineService {

    private GeometryHelper geometryHelper;
    private PartnerBusiness partnerBusiness;
    private PartnerTransformer partnerTransformer;

    public PartnerEngineService( GeometryHelper geometryHelper, PartnerBusiness partnerBusiness, PartnerTransformer partnerTransformer) {
        this.geometryHelper = geometryHelper;
        this.partnerBusiness = partnerBusiness;
        this.partnerTransformer = partnerTransformer;
    }
    @Value("${nearest.maximum-range}")
    private long MAXIMUM_RANGE;

    public PartnerGeoDTO findNearestPartner(Double lng, Double lat) throws IOException {
        log.info("Searching Partners for long {}, lat {} covered by pre-defined range.",lng,lat);
        Point location = geometryHelper.createAnPointFromCoordinate(new Coordinate(lng, lat));
        List<Partner> partnerLocations = partnerBusiness.findNearestCoordinatesFromAnPointWithRange(lng,lat,MAXIMUM_RANGE);
        log.info("Partners found in range -> "+partnerLocations.size());
        if(partnerLocations.size()==0)
            throw new NotFoundException("No Partners found nearby.");
        List<Partner> partnerInCoverArea = partnerLocations.stream().filter(partner -> {
            MultiPolygon multiPolygon = (MultiPolygon)partner.getCoverageArea();
            return geometryHelper.isPointInsideMultiPolygon(location,multiPolygon);
        }).collect(Collectors.toList());
        log.info("Partners where CoverageArea cover -> "+partnerInCoverArea.size());
        if(partnerInCoverArea.size()==1)
            return partnerTransformer.toGeoDtoMapperFromEntity(partnerInCoverArea.get(0));
        log.info("Finding the closest one.");
        //TODO Improve distance with Matrix API to consider roads.
        //Partner closestPartner = partnerBusiness.extractClosestPartnerByAddress(partnerInCoverArea, location);
        Partner closestPartner = partnerBusiness.extractClosestPartnerByAddressAndRoads(partnerInCoverArea, location);
        log.info("Closest -> {}",closestPartner.getTradingName());
        return partnerTransformer.toGeoDtoMapperFromEntity(closestPartner);
    }
}
