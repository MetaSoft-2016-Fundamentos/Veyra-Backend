package com.metasoft.veyra.platform.nursing.application.internal.queryservices;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Administrator;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAdministratorByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.AdministratorQueryService;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.AdministratorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AdministratorQueryServiceImpl implements AdministratorQueryService {
    private final AdministratorRepository administratorRepository;

    public AdministratorQueryServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }


    @Override
    public Optional<Administrator> handle(GetAdministratorByIdQuery query) {
        return administratorRepository.findById(query.id());
    }
}
