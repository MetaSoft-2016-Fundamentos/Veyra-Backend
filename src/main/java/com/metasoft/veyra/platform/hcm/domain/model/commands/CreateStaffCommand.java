package com.metasoft.veyra.platform.hcm.domain.model.commands;

import java.time.LocalDate;

public record CreateStaffCommand(Long nursingHomeId,String dni, String firstName, String lastName,
                                 LocalDate birthDate, Integer Age, String emailAddress, String street,
                                 String number,
                                 String city,
                                 String postalCode,
                                 String country,
                                 byte[] photoData,
                                 String photoFileName, String phoneNumber
        , String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber) {
}
