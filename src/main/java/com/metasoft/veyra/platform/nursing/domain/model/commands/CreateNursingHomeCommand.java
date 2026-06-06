package com.metasoft.veyra.platform.nursing.domain.model.commands;

public record CreateNursingHomeCommand(Long administratorId,String businessName,
                                       String emailAddress,
                                       String phoneNumber,
                                       String street,
                                       String number,
                                       String city,
                                       String postalCode,
                                       String country,
                                       byte[] photoData,
                                       String photoFileName,
                                       String ruc ) {
}
