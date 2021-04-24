package com.thejaum.challenge.partner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "coordinates",schema = "data")
//TODO Change name, are mistaken with locationtech objects.
public class Coordinate {

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
    @JsonBackReference
    private Partner partner;
}
