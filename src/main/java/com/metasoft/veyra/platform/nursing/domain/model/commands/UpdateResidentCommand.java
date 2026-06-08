package com.metasoft.veyra.platform.nursing.domain.model.commands;

import java.time.LocalDate;

public record UpdateResidentCommand(Long id, String dni,String firstName, String lastName,
                                    LocalDate birthDate, Integer Age, String emailAddress, String street,
                                    String number,
                                    String city,
                                    String postalCode,
                                    String country,
                                    byte[] photoData,
                                    String photoFileName, String phoneNumber,
                                    String legalRepresentativeFirstName
        , String legalRepresentativeLastName, String legalRepresentativePhoneNumber
        , String emergencyContactFirstName,
                                    String emergencyContactLastName,
                                    String emergencyContactPhoneNumber) {

    public UpdateResidentCommand{
        if(id==null) {
            throw new IllegalArgumentException(" Cannot is null");
        }
        if (legalRepresentativeFirstName==null|| legalRepresentativeFirstName.isBlank()){
           throw new IllegalArgumentException("");
        }
        if (legalRepresentativeLastName==null|| legalRepresentativeLastName.isBlank()){
         throw  new IllegalArgumentException("");
        }
        if (emergencyContactFirstName==null|| emergencyContactFirstName.isBlank()){throw  new IllegalArgumentException("");}
        if (emergencyContactLastName==null|| emergencyContactLastName.isBlank()){throw new IllegalArgumentException("");}
        if (emergencyContactPhoneNumber==null|| emergencyContactPhoneNumber.isBlank()){throw new IllegalArgumentException("");}
    }
}
