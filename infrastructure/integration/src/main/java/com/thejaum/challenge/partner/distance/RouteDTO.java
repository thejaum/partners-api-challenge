package com.thejaum.challenge.partner.distance;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private double originLat;
    private double originLng;
    private double destinationLat;
    private double destinationLng;
}
