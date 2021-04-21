package com.thejaum.challenge.partner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.postgis.PGgeometry;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "geometry",schema = "data")
public class Geometry {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "id",
            updatable = false,
            unique = true,
            nullable = false
    )
    private UUID id;

    @Column(length = 80)
    private String type;

    @Column(columnDefinition = "geometry")
    PGgeometry geometry;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;
}
