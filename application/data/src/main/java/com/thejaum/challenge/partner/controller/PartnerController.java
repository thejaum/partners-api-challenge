package com.thejaum.challenge.partner.controller;

import com.thejaum.challenge.partner.dto.PartnerLocationsDTO;
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
    public ResponseEntity<PartnerLocationsDTO> findById(@PathVariable UUID id) throws IOException {
        return ResponseEntity.ok(partnerService.findPartnerAndCoordinatesByPartnerId(id));
    }

    @PostMapping
    public ResponseEntity<PartnerLocationsDTO> create(@RequestBody PartnerLocationsDTO partnerLocationsDTO) throws IOException {
        return ResponseEntity.ok(partnerService.registerNewPartner(partnerLocationsDTO));
    }
}
