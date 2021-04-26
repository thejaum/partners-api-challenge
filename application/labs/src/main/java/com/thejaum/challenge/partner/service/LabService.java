package com.thejaum.challenge.partner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.distance.RouteDTO;
import com.thejaum.challenge.partner.distance.RouteEngine;
import com.thejaum.challenge.partner.dto.FeatureCollectionDTO;
import com.thejaum.challenge.partner.dto.FeatureDTO;
import com.thejaum.challenge.partner.dto.PdvPartnersDTO;
import com.thejaum.challenge.partner.util.GeometryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class LabService {

    private PartnerService partnerService;
    private ObjectMapper objectMapper;
    private GeometryHelper geometryHelper;
    private RouteEngine routeEngine;

    public LabService(PartnerService partnerService, ObjectMapper objectMapper, GeometryHelper geometryHelper, RouteEngine routeEngine) {
        this.partnerService = partnerService;
        this.objectMapper = objectMapper;
        this.geometryHelper = geometryHelper;
        this.routeEngine = routeEngine;
    }

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
                String color = randColor();
                featureCollectionDTO.getFeatures().add(
                    FeatureDTO.builder()
                            .type("Feature")
                            .geometry(partnerDTO1.getAddress())
                            .properties(createProperties(0,partnerDTO1.getTradingName(),color))
                            .build()
                );
                featureCollectionDTO.getFeatures().add(
                        FeatureDTO.builder()
                                .type("Feature")
                                .geometry(partnerDTO1.getCoverageArea())
                                .properties(createProperties(1,partnerDTO1.getTradingName(),color))
                                .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("FeatureJson");
        System.out.println(this.objectMapper.writeValueAsString(featureCollectionDTO));
    }

    private Map<String,Object> createProperties(int type,String label,String color){
        if(type==0){
            Map<String,Object> properties = new HashMap<>();
            properties.put("marker-color",color);
            properties.put("marker-size","medium");
            properties.put("marker-symbol","beer");
            properties.put("label",label);
            return properties;
        }else{
            Map<String,Object> properties = new HashMap<>();
            properties.put("stroke","#555555");
            properties.put("fill",color);
            properties.put("stroke-width",1);
            properties.put("stroke-opacity",1);
            properties.put("fill-opacity",0.5);
            properties.put("label",label);
            return properties;
        }
    }

    private String randColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        String hex = "#"+Integer.toHexString(randomColor.getRGB()).substring(2);
        System.out.println(hex);
        return hex;
    }

    @PostConstruct
    public void checkTomTom(){
        RouteDTO routeDTO = RouteDTO.builder()
                .origemLat(-23.5756)
                .origemLng(-46.6370)
                .destinationLat(-23.6110)
                .destinationLng(-46.6240)
                .build();
        double routeBetweenTwoLocations = routeEngine.findTravelTimeInSecondsBetweenTwoLocations(routeDTO);
    }
}
