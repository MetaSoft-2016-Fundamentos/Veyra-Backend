package com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
    List<Location> findByDeviceId(Long deviceId);
}
