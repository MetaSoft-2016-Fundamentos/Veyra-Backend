package com.metasoft.veyra.platform.profiles.interfaces.acl;

import java.time.LocalDate;

public interface ProfilesContextFacade {
    Long createPersonProfile(String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
                             String number,
                             String city,
                             String postalCode,
                             String country, byte[] photoData,
                             String photoFileName, String phoneNumber);

    Long fetchPersonProfileIdByDni(String dni);
    Long updatePersonProfile(Long id,String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
                             String number,
                             String city,
                             String postalCode,
                             String country, byte[] photoData,
                             String photoFileName, String phoneNumber);
    void  deletePersonProfile(Long id);
    Long createBusinessProfile(String businessName,
                               String emailAddress,
                               String phoneNumber,
                               String street,
                               String number,
                               String city,
                               String postalCode,
                               String country,
                               byte[] photoData,
                               String photoFileName,
                               String ruc );
    Long fetchBusinessProfileIdByRuc(String ruc);
}
