package com.thejaum.challenge.partner.repository;

import com.thejaum.challenge.partner.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, UUID> {

    @Query(value="select p.id, p.trading_name, p.owner_name, p.\"document\", p.address, p.coverage_area " +
            "from data.partners p where ST_Distance(p.address,ST_Point( :lng, :lat)) < :range"
            ,nativeQuery = true)
    List<Partner> findNearestCoordinatesFromAnPointWithRange(
            @Param("lng") Double lng,
            @Param("lat")  Double lat,
            @Param("range")  Long range);

    Optional<Partner> findByDocument(String document);

}
