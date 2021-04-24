package com.thejaum.challenge.partner.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partners",schema = "data")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@partnerId")
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
    
}
