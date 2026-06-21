package com.metasoft.veyra.platform.health.application.internal.commandservices;

import com.metasoft.veyra.platform.health.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.health.domain.model.aggregates.MedicalCondition;
import com.metasoft.veyra.platform.health.domain.model.commands.RegisterMedicalConditionCommand;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.DiagnosisStatus;
import com.metasoft.veyra.platform.health.domain.services.MedicalConditionCommandService;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.MedicalConditionRepository;
import org.springframework.stereotype.Service;

@Service
class MedicalConditionCommandServiceImpl implements MedicalConditionCommandService {

  private final MedicalConditionRepository medicalConditionRepository;
  private final ExternalNursingService externalNursingService;

  public MedicalConditionCommandServiceImpl(MedicalConditionRepository medicalConditionRepository, ExternalNursingService externalNursingService) {
    this.medicalConditionRepository = medicalConditionRepository;
    this.externalNursingService = externalNursingService;
  }

  @Override
  public Long handle(RegisterMedicalConditionCommand command) {
    var residentIdOpt = externalNursingService.fetchResidentById(command.residentId());

    if (residentIdOpt.isEmpty()) {
      throw new IllegalArgumentException("Resident id not found in Nursing Context");
    }

    if (medicalConditionRepository.existsByResidentIdAndDiagnosisName(residentIdOpt.get(), command.diagnosisName())) {
      throw new IllegalArgumentException("Medical condition already exists for this resident");
    }

    var medicalCondition = new MedicalCondition(
      residentIdOpt.get(),
      command.diagnosisName(),
      command.diagnosisDate(),
      DiagnosisStatus.valueOf(command.status()),
      command.notes()
    );

    try {
      medicalConditionRepository.save(medicalCondition);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to save medical condition record. Please try again.");
    }

    return medicalCondition.getId();
  }
}
