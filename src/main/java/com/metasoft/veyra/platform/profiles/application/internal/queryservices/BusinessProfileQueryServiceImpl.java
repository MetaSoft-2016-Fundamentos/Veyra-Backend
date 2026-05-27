package com.metasoft.veyra.platform.profiles.application.internal.queryservices;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetAllBusinessProfileQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetBusinessProfileByIdQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetBusinessProfileByRucQuery;
import com.metasoft.veyra.platform.profiles.domain.services.BusinessProfileQueryService;
import com.metasoft.veyra.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessProfileQueryServiceImpl implements BusinessProfileQueryService {
    private final BusinessProfileRepository businessProfileRepository;

    public BusinessProfileQueryServiceImpl(BusinessProfileRepository businessProfileRepository) {
        this.businessProfileRepository = businessProfileRepository;
    }

    @Override
    public Optional<BusinessProfile> handle(GetBusinessProfileByIdQuery query) {
        return businessProfileRepository.findById(query.id());
    }

    @Override
    public List<BusinessProfile> handle(GetAllBusinessProfileQuery query) {
        return businessProfileRepository.findAll();
    }

    @Override
    public Optional<BusinessProfile> handle(GetBusinessProfileByRucQuery query) {

        return businessProfileRepository.findByRuc(query.ruc());
    }
}
