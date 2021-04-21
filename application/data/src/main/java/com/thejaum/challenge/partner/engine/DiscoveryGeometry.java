package com.thejaum.challenge.partner.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.dto.PartnerDTO;
import com.thejaum.challenge.partner.model.Coordenate;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.repository.CoordenateRepository;
import com.thejaum.challenge.partner.repository.PartnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.geojson.GeoJsonObject;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.postgis.GeometryBuilder;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.stereotype.Component;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.annotation.PostConstruct;
import java.io.*;
import java.sql.SQLException;
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

    @PostConstruct
    void listPartner() throws IOException {
        final Optional<Partner> partner = partnerRepository.findById(UUID.fromString("5db30734-b7e8-46e0-8d8e-5a3fafb9b738"));
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
