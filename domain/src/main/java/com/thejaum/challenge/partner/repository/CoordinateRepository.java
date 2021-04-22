package com.thejaum.challenge.partner.repository;

import com.thejaum.challenge.partner.model.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, UUID> {

    List<Coordinate> findByPartnerId(UUID id);

}
