package com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.PersonProfileId;
import com.metasoft.veyra.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
@Service("externalProfileServiceHcm")
public class ExternalProfileService {
    private final ProfilesContextFacade profilesContextFacade;

    public ExternalProfileService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
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
    }}
