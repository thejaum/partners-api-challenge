package com.thejaum.challenge.partner.controller;

import com.thejaum.challenge.partner.dto.PartnerGeoDTO;
import com.thejaum.challenge.partner.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/partners")
public class PartnerController {

    private PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PartnerGeoDTO> findById(@PathVariable UUID id) throws IOException {
        return ResponseEntity.ok(partnerService.findPartnerAndCoordinatesByPartnerId(id));
    }

    @PostMapping
    public ResponseEntity<PartnerGeoDTO> create(@RequestBody PartnerGeoDTO partnerGeoDTO) throws IOException {
        return ResponseEntity.ok(partnerService.registerNewPartner(partnerGeoDTO));
    }
}
