package com.thejaum.challenge.partner.service;

import com.thejaum.challenge.partner.business.CoordinateBusiness;
import com.thejaum.challenge.partner.model.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PartnerEngineService {

    private CoordinateBusiness coordinateBusiness;

    public PartnerEngineService(CoordinateBusiness coordinateBusiness) {
        this.coordinateBusiness = coordinateBusiness;
    }

    public List<Coordinate> findNearestPartner(Double lng, Double lat){
        List<Coordinate> nearestCoordinatesFromAnPointWithRange = coordinateBusiness.findNearestCoordinatesFromAnPointWithRange(lng,lat);
        log.info("Size -> "+nearestCoordinatesFromAnPointWithRange.size());
        return nearestCoordinatesFromAnPointWithRange;
    }
}