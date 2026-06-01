package com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Measurement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends MongoRepository<Measurement,String> {
}
