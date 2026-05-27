package com.metasoft.veyra.platform.profiles.interfaces.rest.resources;

import java.time.LocalDate;

public record CreatePersonProfileResource(String dni, String firstName, String lastName, LocalDate birthDate, Integer age, String emailAddress, String street,
                                          String number,
                                          String city,
                                          String postalCode,
                                          String country,String photoBase64,
                                           String phoneNumber) {
}
