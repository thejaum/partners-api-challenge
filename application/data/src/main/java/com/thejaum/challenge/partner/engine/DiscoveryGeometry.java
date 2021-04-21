package com.thejaum.challenge.partner.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.model.Coordenate;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.repository.CoordenateRepository;
import com.thejaum.challenge.partner.repository.PartnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.postgis.GeometryBuilder;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@Component
public class DiscoveryGeometry {

    private ObjectMapper objectMapper;
    private PartnerRepository partnerRepository;
    private CoordenateRepository coordenateRepository;

    public DiscoveryGeometry(ObjectMapper objectMapper, PartnerRepository partnerRepository, CoordenateRepository coordenateRepository) {
        this.objectMapper = objectMapper;
        this.partnerRepository = partnerRepository;
        this.coordenateRepository = coordenateRepository;
    }

    @PostConstruct
    void labGeoFromJson() throws IOException, SQLException {
        File file = new File("F:\\desenv\\challenges\\partners-api-challenge\\docs\\single-partners-pdv.json");
        final PartnerDTO partnerDTO = this.objectMapper.readValue(file, PartnerDTO.class);
        log.info("Owner Name -> "+partnerDTO.getOwnerName());
        Partner save = partnerRepository.save(Partner.builder()
                .tradingName(partnerDTO.getTradingName())
                .ownerName(partnerDTO.getOwnerName())
                .document(partnerDTO.getDocument())
                .build());
        log.info("Partner ID -> "+save.getId());

        GeometryJSON gjson = new GeometryJSON();
        String json = objectMapper.writeValueAsString(partnerDTO.getAddress());
        Coordenate save1 = coordenateRepository.save(Coordenate.builder()
                .partner(save)
                .geometry(gjson.read(json))
                .build());
        log.info("Address ID -> "+save1.getId());

        json = objectMapper.writeValueAsString(partnerDTO.getCoverageArea());
        Coordenate save2 = coordenateRepository.save(Coordenate.builder()
                .partner(save)
                .geometry(gjson.read(json))
                .build());
        log.info("Multipolyggon ID -> "+save2.getId());
    }
}
