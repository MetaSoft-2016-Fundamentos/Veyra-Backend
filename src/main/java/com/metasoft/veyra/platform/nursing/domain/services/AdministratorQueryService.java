package com.metasoft.veyra.platform.nursing.domain.services;


import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Administrator;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAdministratorByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAdministratorByUserIdQuery;

import java.util.Optional;

public interface AdministratorQueryService {
  Optional<Administrator> handle(GetAdministratorByIdQuery query);
  Optional<Administrator>handle(GetAdministratorByUserIdQuery query);
}
