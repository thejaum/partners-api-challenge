package com.thejaum.challenge.partner.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.model.Coordenate;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.repository.CoordenateRepository;
import com.thejaum.challenge.partner.repository.PartnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.geojson.GeoJsonObject;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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


    void labGeoFromJson() throws IOException {
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

    void listPartner() throws IOException {
        final Optional<Partner> partner = partnerRepository.findById(UUID.fromString("27e9a8c1-b4e8-474d-9449-84456d7fea17"));
        final List<Coordenate> coordenates = coordenateRepository.findByPartnerId(partner.get().getId());
        GeometryJSON gjson = new GeometryJSON();

        GeoJsonObject address=null;
        List<Coordenate> collectAddress = coordenates.stream()
                .filter(coordenate -> coordenate.getGeometry().getGeometryType().equals(Geometry.TYPENAME_POINT)).collect(Collectors.toList());
        try (StringWriter stWriterF = new StringWriter()){
            gjson.write(collectAddress.get(0).getGeometry(),stWriterF);
            log.info("did");
            address = this.objectMapper.readValue(stWriterF.toString(), GeoJsonObject.class);
            log.info("did2");
        }
        GeoJsonObject coverageArea=null;
        List<Coordenate> collectMultiPolygon = coordenates.stream()
                .filter(coordenate -> coordenate.getGeometry().getGeometryType().equals(Geometry.TYPENAME_MULTIPOLYGON)).collect(Collectors.toList());
        try (StringWriter stWriterF = new StringWriter()){
            gjson.write(collectMultiPolygon.get(0).getGeometry(),stWriterF);
            log.info("did");
            coverageArea = this.objectMapper.readValue(stWriterF.toString(), GeoJsonObject.class);
            log.info("did2");
        }

        PartnerDTO build = PartnerDTO.builder()
                .document(partner.get().getDocument())
                .ownerName(partner.get().getOwnerName())
                .tradingName(partner.get().getTradingName())
                .address(address)
                .coverageArea(coverageArea)
                .build();
        log.info("PartnerDTO -> "+build.toString());
        log.info("PartnerDTO Json -> "+objectMapper.writeValueAsString(build));
    }
}
