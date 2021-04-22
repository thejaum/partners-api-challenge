package com.thejaum.challenge.partner.business;

import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.repository.PartnerRepository;
import com.thejaum.challenge.partner.transformer.PartnerTransformer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PartnerBusiness {

    private PartnerRepository partnerRepository;
    private PartnerTransformer partnerTransformer;

    public PartnerBusiness(PartnerRepository partnerRepository, PartnerTransformer partnerTransformer) {
        this.partnerRepository = partnerRepository;
        this.partnerTransformer = partnerTransformer;
    }

    public Partner createNewPartner(PartnerDTO partnerDTO){
        Partner partner = partnerRepository.save(partnerTransformer.toEntityMapper(partnerDTO,Partner.class));
        return partner;
    }

    public Partner findPartnerById(UUID id){
        Optional<Partner> partner = partnerRepository.findById(id);
        if(partner.isPresent())
            return partner.get();
        return null;
    }

}
