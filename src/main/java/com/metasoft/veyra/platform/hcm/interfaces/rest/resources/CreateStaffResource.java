package com.metasoft.veyra.platform.hcm.interfaces.rest.resources;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record CreateStaffResource(String dni, String firstName, String lastName,
                                  LocalDate birthDate, Integer age, String emailAddress, String street,
                                  String number,
                                  String city,
                                  String postalCode,
                                  String country,
                                  MultipartFile photo, String phoneNumber
        , String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber) {
}
