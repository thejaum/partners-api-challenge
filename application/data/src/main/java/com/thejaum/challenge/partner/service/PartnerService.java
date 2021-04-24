package com.thejaum.challenge.partner.service;

import com.thejaum.challenge.partner.business.PartnerBusiness;
import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.dto.PartnerGeoDTO;
import com.thejaum.challenge.partner.exception.NotFoundException;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.transformer.PartnerTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PartnerService {

    private PartnerBusiness partnerBusiness;
    private PartnerTransformer partnerTransformer;

    public PartnerService(PartnerBusiness partnerBusiness, PartnerTransformer partnerTransformer) {
        this.partnerBusiness = partnerBusiness;
        this.partnerTransformer = partnerTransformer;
    }

    public PartnerGeoDTO registerNewPartner(PartnerGeoDTO partnerGeoDTO) throws IOException {
        log.info("Beginning to register a new Partner.");
        PartnerDTO partnerDTO = partnerTransformer.toDtoMapperFromGeo(partnerGeoDTO);
        final Partner newPartner = partnerBusiness.createNewPartner(partnerDTO);
        partnerGeoDTO.setId(newPartner.getId());
        return partnerGeoDTO;
    }

    public PartnerGeoDTO findPartnerAndCoordinatesByPartnerId(UUID partnerId) throws IOException {
        log.info("Searching a Partner of id {}",partnerId);
        final Optional<Partner> partner = partnerBusiness.findPartnerById(partnerId);
        if(!partner.isPresent())
            throw new NotFoundException("Partner ID not found.");
        log.info("Partner found.");
        return partnerTransformer.toGeoDtoMapperFromDto(partnerTransformer.toDtoMapper(partner.get(), PartnerDTO.class));
    }

}
