package com.metasoft.veyra.platform.nursing.application.internal.commandservices;

import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.nursing.domain.exceptions.AdministratorAlreadyExistsException;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Administrator;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateAdministratorCommand;
import com.metasoft.veyra.platform.nursing.domain.services.AdministratorCommandService;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.AdministratorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdministratorCommandServiceImpl implements AdministratorCommandService {
    private final AdministratorRepository administratorRepository;
   private final ExternalIamService externalIamService;
    public AdministratorCommandServiceImpl(AdministratorRepository administratorRepository, ExternalIamService externalIamService) {
        this.administratorRepository = administratorRepository;
        this.externalIamService = externalIamService;
    }
    @Override
    public Long handle(CreateAdministratorCommand command) {
        var userId = externalIamService.createUser(
                command.username(),
                command.password(),
                List.of("ROLE_ADMIN")
        );
        if (administratorRepository.existsByUserId(userId)) {
            throw new AdministratorAlreadyExistsException(userId.userId());
        }

        var administrator = new Administrator(userId);
        administratorRepository.save(administrator);

        return administrator.getId();
    }
}
