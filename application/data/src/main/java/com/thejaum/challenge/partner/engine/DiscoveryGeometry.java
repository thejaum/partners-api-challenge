package com.thejaum.challenge.partner.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.dto.PartnerCoordinatesDTO;
import com.thejaum.challenge.partner.repository.CoordinateRepository;
import com.thejaum.challenge.partner.repository.PartnerRepository;
import com.thejaum.challenge.partner.service.PartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class DiscoveryGeometry {

    private ObjectMapper objectMapper;
    private PartnerRepository partnerRepository;
    private CoordinateRepository coordinateRepository;
    private PartnerService partnerService;

    public DiscoveryGeometry(ObjectMapper objectMapper, PartnerRepository partnerRepository, CoordinateRepository coordinateRepository, PartnerService partnerService) {
        this.objectMapper = objectMapper;
        this.partnerRepository = partnerRepository;
        this.coordinateRepository = coordinateRepository;
        this.partnerService = partnerService;
    }


    void labGeoFromJson() throws IOException {
        File file = new File("F:\\desenv\\challenges\\partners-api-challenge\\docs\\single-partners-pdv.json");
        final PartnerCoordinatesDTO partnerCoordinatesDTO = this.objectMapper.readValue(file, PartnerCoordinatesDTO.class);
        log.info("Owner Name -> "+ partnerCoordinatesDTO.getOwnerName());
        partnerService.registerNewPartner(partnerCoordinatesDTO);
    }
    @PostConstruct
    void listPartner() throws IOException {
        partnerService.findPartnerAndCoordinatesByPartnerId(UUID.fromString("aed77f81-7ad9-4dc9-ab5b-1a5c8654b03a"));
    }
}
