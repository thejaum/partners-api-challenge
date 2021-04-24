package com.thejaum.challenge.partner.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partner_locations",schema = "data")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@partnerLocationId")
public class PartnerLocation {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "id",
            updatable = false,
            unique = true,
            nullable = false,
            columnDefinition = "UUID"
    )
    private UUID id;

    @Column(columnDefinition = "Geometry", nullable = true)
    Geometry geometry;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}
