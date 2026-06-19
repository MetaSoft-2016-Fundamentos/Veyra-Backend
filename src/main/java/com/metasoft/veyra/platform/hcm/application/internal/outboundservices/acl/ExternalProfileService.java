package com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.PersonProfileId;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.metasoft.veyra.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.metasoft.veyra.platform.profiles.domain.services.PersonProfileQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
@Service("externalProfileServiceHcm")
public class ExternalProfileService {
    private final ProfilesContextFacade profilesContextFacade;
    private final PersonProfileQueryService personProfileQueryService;

    public ExternalProfileService(ProfilesContextFacade profilesContextFacade, PersonProfileQueryService personProfileQueryService) {
        this.profilesContextFacade = profilesContextFacade;
        this.personProfileQueryService = personProfileQueryService;
    }
    public Optional<PersonProfileId>fetchProfileByDni(String dni){
        var personProfileId=profilesContextFacade.fetchPersonProfileIdByDni(dni);
        return personProfileId==0L?Optional.empty():Optional.of(new PersonProfileId(personProfileId));
    }

    public Optional<PersonProfileId>createPersonProfile(String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
                                                        String number,
                                                        String city,
                                                        String postalCode,
                                                        String country, byte[] photoData,
                                                        String photoFileName, String phoneNumber){
        var personProfileId= profilesContextFacade.createPersonProfile(dni,firstName,lastName,birthDate,Age,emailAddress,street,number,city,postalCode,country,photoData,photoFileName,phoneNumber);
        return personProfileId==0L?Optional.empty():Optional.of(new PersonProfileId(personProfileId));
    }
    public void updatePersonProfile(Long id, String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
                                    String number,
                                    String city,
                                    String postalCode,
                                    String country, byte[] photoData,
                                    String photoFileName, String phoneNumber){
        profilesContextFacade.updatePersonProfile(id,dni,firstName,lastName,birthDate,Age
                ,emailAddress,street,number,city,postalCode,country,photoData,photoFileName,phoneNumber);
    }
    public String fetchDniByPersonProfileId(Long personProfileId) {
        var query = new GetPersonProfileByIdQuery(personProfileId);
        var result = personProfileQueryService.handle(query);
        return result.map(profile -> profile.getDni().dni()).orElse("");
    }
}