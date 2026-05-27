package com.metasoft.veyra.platform.nursing.domain.model.commands;

import java.time.LocalDate;

public record CreateResidentCommand(Long nursingHomeId,String dni, String firstName, String lastName,
                                    LocalDate birthDate, Integer age, String emailAddress, String street,
                                    String number,
                                    String city,
                                    String postalCode,
                                    String country,
                                    byte[] photoData,
                                    String photoFileName, String phoneNumber,String legalRepresentativeFirstName
        ,String legalRepresentativeLastName,String legalRepresentativePhoneNumber
        ,String emergencyContactFirstName,String emergencyContactLastName,String emergencyContactPhoneNumber) {
}
