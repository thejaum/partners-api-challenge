package com.thejaum.challenge.partner.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorDetailsDTO {
    private String fieldName;
    private String description;
}
