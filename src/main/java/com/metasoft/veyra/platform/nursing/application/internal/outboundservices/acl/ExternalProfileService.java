package com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.BusinessProfileId;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.PersonProfileId;
import com.metasoft.veyra.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExternalProfileService {
    private final ProfilesContextFacade profilesContextFacade;

    public ExternalProfileService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }
    public Optional<PersonProfileId>fetchProfileByDni(String dni){
        var personProfileID=profilesContextFacade.fetchPersonProfileIdByDni(dni);
        return personProfileID==0L?Optional.empty():Optional.of(new PersonProfileId(personProfileID));
    }
    public Optional<PersonProfileId>createPersonProfile(
            String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
            String number,
            String city,
            String postalCode,
            String country, byte[] photoData,
            String photoFileName, String phoneNumber)
    {
        var personProfileId= profilesContextFacade.createPersonProfile(dni,firstName,lastName,birthDate,Age
        ,emailAddress,street,number,city,postalCode,country,photoData,photoFileName,phoneNumber);
        return personProfileId==0L?Optional.empty():Optional.of(new PersonProfileId(personProfileId));
    }
    public Optional<BusinessProfileId>fetchBusinessProfileByRuc(String ruc){
        var businessProfileId=profilesContextFacade.fetchBusinessProfileIdByRuc(ruc);
        return businessProfileId==0L?Optional.empty():Optional.of(new BusinessProfileId(businessProfileId));
    }

    public Optional<BusinessProfileId>createBusinessProfile(String businessName,
                                                            String emailAddress,
                                                            String phoneNumber,
                                                            String street,
                                                            String number,
                                                            String city,
                                                            String postalCode,
                                                            String country,
                                                            byte[] photoData,
                                                            String photoFileName,
                                                            String ruc )
    {
        var businessProfileId=profilesContextFacade.createBusinessProfile(businessName,emailAddress,phoneNumber,street,number,city,postalCode,country,photoData,photoFileName, ruc);
        return businessProfileId==0L?Optional.empty():Optional.of(new BusinessProfileId(businessProfileId));

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
    public void deletePersonProfile(Long personProfileId) {
        profilesContextFacade.deletePersonProfile(personProfileId);
    }

}
