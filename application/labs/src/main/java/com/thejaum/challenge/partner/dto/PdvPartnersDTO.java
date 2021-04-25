package com.thejaum.challenge.partner.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PdvPartnersDTO {

    private List<PartnerGeoDTO> pdvs;

}
