package com.thejaum.challenge.partner.controller;


import com.thejaum.challenge.partner.dto.PartnerGeoDTO;
import com.thejaum.challenge.partner.service.PartnerEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/v1/partners")
public class PartnerEngineController {

    private PartnerEngineService partnerEngineService;

    public PartnerEngineController(PartnerEngineService partnerEngineService) {
        this.partnerEngineService = partnerEngineService;
    }

    @GetMapping
    public ResponseEntity<PartnerGeoDTO> searchNearest(@RequestParam(value = "long", required = true) Double lng,
                                                       @RequestParam(value = "lat", required = true) Double lat) throws IOException {
        return ResponseEntity.ok(partnerEngineService.findNearestPartner(lng,lat));
    }
}
