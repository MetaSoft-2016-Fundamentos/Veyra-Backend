package com.metasoft.veyra.platform.profiles.interfaces.rest.resources;

import java.time.LocalDate;

public record PersonProfileResource(Long id,
                                    String fullName
                                    , String dni,
                                    LocalDate birthDate
                                    , Integer age,
                                    String photo,
                                    String phoneNumber,
                                    String emailAddress,
                                    String streetAddress) {
}
