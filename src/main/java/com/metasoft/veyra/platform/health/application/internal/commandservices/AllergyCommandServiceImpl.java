package com.metasoft.veyra.platform.health.application.internal.commandservices;
import com.metasoft.veyra.platform.health.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.health.domain.model.aggregates.Allergy;
import com.metasoft.veyra.platform.health.domain.model.commands.RegisterAllergyCommand;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.SeverityLevel;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.TypeOfAllergy;
import com.metasoft.veyra.platform.health.domain.services.AllergyCommandService;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.AllergyRepository;
import org.springframework.stereotype.Service;
@Service
public class AllergyCommandServiceImpl implements AllergyCommandService {
private  final AllergyRepository allergyRepository;
private final ExternalNursingService externalNursingService;
    public AllergyCommandServiceImpl(AllergyRepository allergyRepository, ExternalNursingService externalNursingService) {
        this.allergyRepository = allergyRepository;
        this.externalNursingService = externalNursingService;
    }
    @Override
    public Long handle(RegisterAllergyCommand command) {
        var residentId=externalNursingService.fetchResidentById(command.residentId());
        if (residentId.isEmpty()){
            throw new IllegalArgumentException("resident id not found");
        }
      if (allergyRepository.existsByResidentIdAndAllergenName(residentId.get(),command.allergenName())){
          throw new IllegalArgumentException(" allergy already exists");
      }
     var allergy=new Allergy(residentId.get(),command.reaction(),command.allergenName(), TypeOfAllergy.valueOf(command.typeOfAllergy()), SeverityLevel.valueOf(command.severityLevel()));
      try {
          allergyRepository.save(allergy);
      }catch( Exception e){
          throw new IllegalArgumentException("Failed to save allergy record. Please try again.");
      }
        return allergy.getId();
    }
}
