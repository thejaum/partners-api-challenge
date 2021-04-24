package com.thejaum.challenge.partner.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejaum.challenge.partner.model.Partner;
import com.thejaum.challenge.partner.model.PartnerLocation;
import com.thejaum.challenge.partner.repository.CoordinateRepository;
import org.geojson.GeoJsonObject;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CoordinateBusiness {
    private ObjectMapper objectMapper;
    private CoordinateRepository coordinateRepository;
    private final Long MAXIMUM_RANGE=3000L;

    public CoordinateBusiness(ObjectMapper objectMapper, CoordinateRepository coordinateRepository) {
        this.objectMapper = objectMapper;
        this.coordinateRepository = coordinateRepository;
    }

    public PartnerLocation createNewCoordinate(GeoJsonObject geoJsonObject, Partner partner) throws IOException {
        GeometryJSON gjson = new GeometryJSON();
        String json = objectMapper.writeValueAsString(geoJsonObject);
        PartnerLocation partnerLocation = coordinateRepository.save(PartnerLocation.builder()
                .partner(partner)
                .geometry(gjson.read(json))
                .build());
        return partnerLocation;
    }

    public List<PartnerLocation> findCoordinatesByPartnerId(UUID partnerId){
        List<PartnerLocation> coordinaties = coordinateRepository.findByPartnerId(partnerId);
        return coordinaties;
    }

    public GeoJsonObject GeometryToGeoJson(Geometry geometry) throws IOException {
        GeoJsonObject geoJsonObject=null;
        GeometryJSON gjson = new GeometryJSON();
        try (StringWriter stWriterF = new StringWriter()){
            gjson.write(geometry,stWriterF);
            geoJsonObject = this.objectMapper.readValue(stWriterF.toString(), GeoJsonObject.class);
        }
        return geoJsonObject;
    }

    public PartnerLocation searchFirstCoordinateInListByType(List<PartnerLocation> partnerLocations, String geometryType){
        List<PartnerLocation> filtredPartnerLocation = partnerLocations.stream()
                .filter(coordinate -> coordinate.getGeometry().getGeometryType().equals(geometryType)).collect(Collectors.toList());
        return filtredPartnerLocation.get(0);
    }

    public List<PartnerLocation> findNearestCoordinatesFromAnPointWithRange(Double lng, Double lat) {
        return coordinateRepository.findNearestCoordinatesFromAnPointWithRange(lng, lat, MAXIMUM_RANGE);
    }

    public PartnerLocation findCoverageAreaByPartnerId(UUID partnerId) {
        return coordinateRepository.findCoverageAreaByPartnerId(partnerId);
    }
}
