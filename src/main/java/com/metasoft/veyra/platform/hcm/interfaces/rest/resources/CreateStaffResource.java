package com.metasoft.veyra.platform.hcm.interfaces.rest.resources;

import java.time.LocalDate;

public record CreateStaffResource(String dni, String firstName, String lastName,
                                  LocalDate birthDate, Integer age, String emailAddress, String street,
                                  String number,
                                  String city,
                                  String postalCode,
                                  String country,
                                  String photoBase64, String phoneNumber
        , String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber) {
}
