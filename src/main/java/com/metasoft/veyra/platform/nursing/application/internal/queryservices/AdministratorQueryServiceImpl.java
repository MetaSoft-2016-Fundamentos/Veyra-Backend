package com.metasoft.veyra.platform.nursing.application.internal.queryservices;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Administrator;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAdministratorByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAdministratorByUserIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
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

  @Override
  public Optional<Administrator> handle(GetAdministratorByUserIdQuery query) {
    var userId= new UserId(query.userId());
    return administratorRepository.findByUserId(userId);
  }
}
