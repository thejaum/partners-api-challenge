package com.thejaum.challenge.partner.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FeatureCollectionDTO {
    private String type;
    private List<FeatureDTO> features;
}
