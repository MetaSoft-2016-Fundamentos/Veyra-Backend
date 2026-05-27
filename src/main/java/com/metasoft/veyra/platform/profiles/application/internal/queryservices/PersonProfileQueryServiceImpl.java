package com.metasoft.veyra.platform.profiles.application.internal.queryservices;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.PersonProfile;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetAllPersonProfileQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetPersonProfileByDniQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.metasoft.veyra.platform.profiles.domain.services.PersonProfileQueryService;
import com.metasoft.veyra.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonProfileQueryServiceImpl implements PersonProfileQueryService {
  private final PersonProfileRepository personProfileRepository;

    public PersonProfileQueryServiceImpl(PersonProfileRepository personProfileRepository) {
        this.personProfileRepository = personProfileRepository;
    }

    @Override
    public List<PersonProfile> handle(GetAllPersonProfileQuery query) {
        return personProfileRepository.findAll();
    }

    @Override
    public Optional<PersonProfile> handle(GetPersonProfileByIdQuery query) {
      return personProfileRepository.findById(query.id());
    }

    @Override
    public Optional<PersonProfile> handle(GetPersonProfileByDniQuery query) {
        return personProfileRepository.findByDni(query.dni());
    }
}
