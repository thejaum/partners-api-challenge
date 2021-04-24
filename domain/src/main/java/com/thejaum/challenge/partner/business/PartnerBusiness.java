package com.thejaum.challenge.partner.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.repository.PartnerRepository;
import com.thejaum.challenge.partner.transformer.PartnerTransformer;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PartnerBusiness {

    private ObjectMapper objectMapper;
    private PartnerRepository partnerRepository;
    private PartnerTransformer partnerTransformer;

    private final Long MAXIMUM_RANGE=1000L;

    public PartnerBusiness(ObjectMapper objectMapper, PartnerRepository partnerRepository, PartnerTransformer partnerTransformer) {
        this.objectMapper = objectMapper;
        this.partnerRepository = partnerRepository;
        this.partnerTransformer = partnerTransformer;
    }

    public Partner createNewPartner(PartnerDTO partnerDTO){
        Partner partner = partnerRepository.save(partnerTransformer.toEntityMapper(partnerDTO,Partner.class));
        return partner;
    }

    public Optional<Partner> findPartnerById(UUID id){
        Optional<Partner> partner = partnerRepository.findById(id);
        return partner;
    }

    public List<Partner> findNearestCoordinatesFromAnPointWithRange(Double lng, Double lat) {
        return partnerRepository.findNearestCoordinatesFromAnPointWithRange(lng, lat, MAXIMUM_RANGE);
    }

    public Optional<Partner> extractClosestPartnerByAddress(List<Partner> partners, Point point){
        return partners.stream().min(Comparator.comparing(partner -> partner.getAddress().distance(point)));
    }
}
