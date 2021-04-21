package com.thejaum.challenge.partner.repository;

import com.thejaum.challenge.partner.model.Coordenate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoordenateRepository extends JpaRepository<Coordenate, UUID> {

    List<Coordenate> findByPartnerId(UUID id);

}
