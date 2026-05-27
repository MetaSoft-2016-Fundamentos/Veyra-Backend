package com.metasoft.veyra.platform.nursing.application.internal.commandservices;

import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeCommandService;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.RelativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RelativeCommandServiceImpl implements RelativeCommandService {
    private final RelativeRepository relativeRepository;
 private  final ExternalIamService externalIamService;
    public RelativeCommandServiceImpl(RelativeRepository relativeRepository, ExternalIamService externalIamService) {
        this.relativeRepository = relativeRepository;
        this.externalIamService = externalIamService;
    }
    @Override
    public Long handle(CreateRelativeCommand command) {
        var userId = externalIamService.createUser(
                command.username(),
                command.password(),
                List.of("ROLE_FAMILIAR")
        );
        if (relativeRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("Family already exists for this user");
        }
        var familiar = new Relative(userId);
        relativeRepository.save(familiar);
        return familiar.getId();
    }
}