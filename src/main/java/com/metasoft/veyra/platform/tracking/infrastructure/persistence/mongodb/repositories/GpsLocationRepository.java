package com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.GpsLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GpsLocationRepository extends MongoRepository<GpsLocation, String> {
    List<GpsLocation> findByDeviceId(String deviceId);
}
