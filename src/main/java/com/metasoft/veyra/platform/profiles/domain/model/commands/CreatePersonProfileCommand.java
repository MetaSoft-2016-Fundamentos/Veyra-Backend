package com.metasoft.veyra.platform.profiles.domain.model.commands;

import java.time.LocalDate;
public record CreatePersonProfileCommand(String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
                                         String number,
                                         String city,
                                         String postalCode,
                                         String country,byte[] photoData,
                                         String photoFileName, String phoneNumber) {
}
