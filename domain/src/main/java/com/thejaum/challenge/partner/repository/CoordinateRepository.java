package com.thejaum.challenge.partner.repository;

import com.thejaum.challenge.partner.model.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, UUID> {

    List<Coordinate> findByPartnerId(UUID id);

    @Query(value="select c.id, c.geometry, c.partner_id  from data.coordinates c " +
            "where ST_GeometryType(c.geometry) = 'ST_Point' " +
            "and ST_DWithin(c.geometry,ST_Point( :lng , :lat ), :range ,false)",nativeQuery = true)
    List<Coordinate> findNearestCoordinatesFromAnPointWithRange(
            @Param("lng") Double lng,
            @Param("lat")  Double lat,
            @Param("range")  Long range);
}
