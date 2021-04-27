package com.thejaum.challenge.partner.business;

import com.thejaum.challenge.partner.distance.*;
import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.repository.PartnerRepository;
import com.thejaum.challenge.partner.transformer.PartnerTransformer;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PartnerBusiness {

    private PartnerRepository partnerRepository;
    private PartnerTransformer partnerTransformer;
    private List<RouteEngine> routeEngines;

    public PartnerBusiness(PartnerRepository partnerRepository, PartnerTransformer partnerTransformer, List<RouteEngine> routeEngines) {
        this.partnerRepository = partnerRepository;
        this.partnerTransformer = partnerTransformer;
        this.routeEngines = routeEngines;
    }

    public Partner createNewPartner(PartnerDTO partnerDTO){
        Partner partner = partnerRepository.save(partnerTransformer.toEntityMapper(partnerDTO,Partner.class));
        return partner;
    }

    public Optional<Partner> findPartnerById(UUID id){
        Optional<Partner> partner = partnerRepository.findById(id);
        return partner;
    }

    public Optional<Partner> findByDocument(String document){
        Optional<Partner> partner = partnerRepository.findByDocument(document);
        return partner;
    }

    public List<Partner> findNearestCoordinatesFromAnPointWithRange(Double lng, Double lat,long range) {
        return partnerRepository.findNearestCoordinatesFromAnPointWithRange(lng, lat, range);
    }

    public Partner extractClosestPartnerByAddress(List<Partner> partners, Point point){
        return partners.stream().min(Comparator.comparing(partner -> partner.getAddress().distance(point))).get();
    }

    public Partner extractClosestPartnerByAddressAndRoads(List<Partner> partners, Point origim, RouteGateway routeGateway){
        Map<Partner,Long> documentsChecked = new LinkedHashMap<>();
        RouteEngine routeEngineImp = routeEngines.stream()
                .filter(routeEngine -> routeEngine.accept())
                .findFirst().orElseThrow(() -> new InvalidRouteEngineException("Invalid Route Engine Key."));
        for(Partner partner : partners){
            Point address = (Point) partner.getAddress();
            Long seconds = routeEngineImp.findTravelTimeInSecondsBetweenTwoLocations(RouteDTO.builder()
                    .originLng(origim.getX())
                    .originLat(origim.getY())
                    .destinationLng(address.getX())
                    .destinationLat(address.getY())
                    .build());
            documentsChecked.put(partner,seconds);
        };
        return documentsChecked.entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue)).get().getKey();

    }
}
