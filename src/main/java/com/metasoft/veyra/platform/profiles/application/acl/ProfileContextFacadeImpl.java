package com.metasoft.veyra.platform.profiles.application.acl;

import com.metasoft.veyra.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.commands.DeletePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetBusinessProfileByRucQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetPersonProfileByDniQuery;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Dni;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Ruc;
import com.metasoft.veyra.platform.profiles.domain.services.BusinessProfileCommandService;
import com.metasoft.veyra.platform.profiles.domain.services.BusinessProfileQueryService;
import com.metasoft.veyra.platform.profiles.domain.services.PersonProfileCommandService;
import com.metasoft.veyra.platform.profiles.domain.services.PersonProfileQueryService;
import com.metasoft.veyra.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProfileContextFacadeImpl implements ProfilesContextFacade {
    private final PersonProfileCommandService personProfileCommandService;
    private final PersonProfileQueryService personProfileQueryService;
    private final BusinessProfileCommandService businessProfileCommandService;
private final BusinessProfileQueryService businessProfileQueryService;

    public ProfileContextFacadeImpl(PersonProfileCommandService personProfileCommandService, PersonProfileQueryService personProfileQueryService, BusinessProfileCommandService businessProfileCommandService, BusinessProfileQueryService businessProfileQueryService) {
        this.personProfileCommandService = personProfileCommandService;
        this.personProfileQueryService = personProfileQueryService;
        this.businessProfileCommandService = businessProfileCommandService;
        this.businessProfileQueryService = businessProfileQueryService;
    }

    @Override
    public Long createPersonProfile(String dni, String firstName, String lastName,
                                    LocalDate birthDate, Integer Age, String emailAddress,String street, String number,
                                    String city, String postalCode,String country,byte[] photoData,
                                    String photoFileName, String phoneNumber) {
        var createPersonProfileCommand=
                new
                        CreatePersonProfileCommand
                        (dni,firstName,lastName,birthDate,Age,emailAddress,street,number,city,postalCode,country,photoData,photoFileName,phoneNumber);
        var personProfile= personProfileCommandService.handle(createPersonProfileCommand);
        return personProfile.isEmpty()?Long.valueOf(0L):personProfile.get().getId();
    }
    @Override
    public Long createBusinessProfile(String businessName, String emailAddress, String phoneNumber, String street, String number, String city, String postalCode, String country, byte[] photoData,
                                      String photoFileName, String ruc) {
        var createBusinessProfileCommand= new CreateBusinessProfileCommand(businessName,emailAddress,phoneNumber,street,number,city,postalCode,country,photoData,photoFileName,ruc);
        var businessProfile= businessProfileCommandService.handle(createBusinessProfileCommand);
        return businessProfile.isEmpty()?Long.valueOf(0L):businessProfile.get().getId();
    }

    @Override
    public Long fetchBusinessProfileIdByRuc(String ruc) {
        var getBusinessProfileQuery= new GetBusinessProfileByRucQuery(new Ruc(ruc));
          var businessProfile= businessProfileQueryService.handle(getBusinessProfileQuery);
          return businessProfile.isEmpty()?Long.valueOf(0L):businessProfile.get().getId();
    }

    @Override
    public Long fetchPersonProfileIdByDni(String dni) {
        var getProfileByDniQuery= new GetPersonProfileByDniQuery(new Dni(dni));
        var personProfile= personProfileQueryService.handle(getProfileByDniQuery);
        return personProfile.isEmpty()?Long.valueOf(0L):personProfile.get().getId();
    }

    @Override
    public Long updatePersonProfile(Long id,String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street, String number, String city, String postalCode, String country,byte[] photoData,
                                    String photoFileName, String phoneNumber) {
        var updatePersonProfileCommand= new UpdatePersonProfileCommand(id,dni,firstName,lastName,birthDate,Age,emailAddress,street,number,city,postalCode,country,photoData,photoFileName,phoneNumber);
        var personProfile= personProfileCommandService.handle(updatePersonProfileCommand);
        return personProfile.isEmpty()?Long.valueOf(0L):personProfile.get().getId();
    }

    @Override
    public void deletePersonProfile(Long id) {
        var deletePersonProfileCommand=new DeletePersonProfileCommand(id);
        personProfileCommandService.handle(deletePersonProfileCommand);

    }


}
