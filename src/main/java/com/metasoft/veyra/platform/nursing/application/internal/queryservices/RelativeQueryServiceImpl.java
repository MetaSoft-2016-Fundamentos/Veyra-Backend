package com.metasoft.veyra.platform.nursing.application.internal.queryservices;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetRelativeByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeQueryService;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.RelativeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RelativeQueryServiceImpl implements RelativeQueryService {
    private final RelativeRepository relativeRepository;

    public RelativeQueryServiceImpl(RelativeRepository relativeRepository) {
        this.relativeRepository = relativeRepository;
    }

    @Override
    public Optional<Relative> handle(GetRelativeByIdQuery query) {
        return relativeRepository.findById(query.id());
    }
}
