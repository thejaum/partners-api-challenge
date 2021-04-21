package com.thejaum.challenge.partner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Geometry;
import org.postgis.PGgeometry;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coordenates",schema = "data")
public class Coordenate {

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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Partner partner;
}
