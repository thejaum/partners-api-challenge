package com.thejaum.challenge.partner.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partners",schema = "data")
public class Partner {

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

    @Column(name = "trading_name",length = 255)
    private String tradingName;

    @Column(name = "owner_name",length = 255)
    private String ownerName;

    @Column(unique=true)
    private String document;

    @OneToMany(mappedBy="partner")
    @JsonManagedReference
    private List<Coordinate> geoList;
}
