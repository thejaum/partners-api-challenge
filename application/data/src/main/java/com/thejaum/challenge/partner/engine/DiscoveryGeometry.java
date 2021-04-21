package com.thejaum.challenge.partner.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.dto.PartnerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class DiscoveryGeometry {

    private ObjectMapper objectMapper;

    public DiscoveryGeometry(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void labGeoFromJson() throws IOException {
        File file = new File("F:\\desenv\\challenges\\partners-api-challenge\\docs\\single-partners-pdv.json");
        final PartnerDTO partnerDTO = this.objectMapper.readValue(file, PartnerDTO.class);
        log.info("Owner Name -> "+partnerDTO.getOwnerName());
    }
}
