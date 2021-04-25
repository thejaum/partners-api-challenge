package com.thejaum.challenge.partner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.dto.FeatureCollectionDTO;
import com.thejaum.challenge.partner.dto.FeatureDTO;
import com.thejaum.challenge.partner.dto.PdvPartnersDTO;
import com.thejaum.challenge.partner.util.GeometryHelper;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LabService {

    private PartnerService partnerService;
    private ObjectMapper objectMapper;
    private GeometryHelper geometryHelper;

    public LabService(PartnerService partnerService, ObjectMapper objectMapper, GeometryHelper geometryHelper) {
        this.partnerService = partnerService;
        this.objectMapper = objectMapper;
        this.geometryHelper = geometryHelper;
    }

    @PostConstruct
    public void persistBaseTeste() throws IOException {
        File file = new File("F:\\desenv\\challenges\\partners-api-challenge\\docs\\partners-pdv.json");
        final PdvPartnersDTO partnerDTO = this.objectMapper.readValue(file, PdvPartnersDTO.class);
        FeatureCollectionDTO featureCollectionDTO = new FeatureCollectionDTO();
        featureCollectionDTO.setType("FeatureCollection");
        featureCollectionDTO.setFeatures(new ArrayList<>());
        partnerDTO.getPdvs().stream().forEach(partnerDTO1 -> {
            try {
                log.info("Cadastrando -> "+partnerDTO1.getDocument());
                partnerService.registerNewPartner(partnerDTO1);
                featureCollectionDTO.getFeatures().add(
                    FeatureDTO.builder()
                            .type("Feature")
                            .geometry(partnerDTO1.getAddress())
                            .properties(createProperties(0))
                            .build()
                );
                featureCollectionDTO.getFeatures().add(
                        FeatureDTO.builder()
                                .type("Feature")
                                .geometry(partnerDTO1.getCoverageArea())
                                .properties(createProperties(1))
                                .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("FeatureJson");
        System.out.println(this.objectMapper.writeValueAsString(featureCollectionDTO));
    }

    private Map<String,Object> createProperties(int type){
        if(type==0){
            Map<String,Object> properties = new HashMap<>();
            properties.put("marker-color","#7e7e7e");
            properties.put("marker-size","medium");
            properties.put("marker-symbol","beer");
            return properties;
        }else{
            Map<String,Object> properties = new HashMap<>();
            properties.put("stroke","#555555");
            properties.put("stroke-width",1);
            properties.put("stroke-opacity",1);
            properties.put("fill-opacity",0.5);
            return properties;
        }
    }
}
