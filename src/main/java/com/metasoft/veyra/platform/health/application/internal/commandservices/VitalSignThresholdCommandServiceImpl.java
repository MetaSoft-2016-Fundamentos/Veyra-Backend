package com.metasoft.veyra.platform.health.application.internal.commandservices;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSignThreshold;
import com.metasoft.veyra.platform.health.domain.model.commands.RegisterVitalSignThresholdCommand;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.services.VitalSignThresholdCommandService;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.VitalSignThresholdRepository;
import org.springframework.stereotype.Service;

@Service
public class VitalSignThresholdCommandServiceImpl implements VitalSignThresholdCommandService {

    private final VitalSignThresholdRepository repository;

    public VitalSignThresholdCommandServiceImpl(VitalSignThresholdRepository repository) {
        this.repository = repository;
    }

    @Override
    public VitalSignThreshold handle(RegisterVitalSignThresholdCommand command) {
        var residentId = new ResidentId(command.residentId());
        var existing = repository.findByResidentId(residentId);

        if (existing.isPresent()) {
            var threshold = existing.get();
            threshold.update(
                    command.heartRateMin(), command.heartRateMax(),
                    command.systolicMax(), command.diastolicMax(),
                    command.temperatureMin(), command.temperatureMax(),
                    command.oxygenSaturationMin(),
                    command.respiratoryRateMin(), command.respiratoryRateMax()
            );
            return repository.save(threshold);
        }

        var threshold = new VitalSignThreshold(
                residentId,
                command.heartRateMin(), command.heartRateMax(),
                command.systolicMax(), command.diastolicMax(),
                command.temperatureMin(), command.temperatureMax(),
                command.oxygenSaturationMin(),
                command.respiratoryRateMin(), command.respiratoryRateMax()
        );
        return repository.save(threshold);
    }
}
