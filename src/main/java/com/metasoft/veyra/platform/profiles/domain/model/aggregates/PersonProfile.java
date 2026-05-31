package com.metasoft.veyra.platform.profiles.domain.model.aggregates;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.*;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.PersonName;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class PersonProfile extends Profile{
    @Embedded
    private PersonName personName;
    @Embedded
    private BirthDate birthDate;
    @Embedded
    private Age age;
    @Embedded
    private Dni dni;
    protected PersonProfile(){super();}
    public PersonProfile(String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
                         String number,
                         String city,
                         String postalCode,
                         String country,String photoUrl,
                         String photoPublicId, String phoneNumber){
        super(emailAddress,street,number,city,postalCode,country,new Photo(photoUrl,photoPublicId),phoneNumber);
        this.personName= new PersonName(firstName,lastName);
        this.birthDate=new BirthDate(birthDate);
        this.age= new Age(Age);
        this.dni=new Dni(dni);
    }
    public PersonProfile updatePersonProfile(String dni, String firstName, String lastName, LocalDate birthDate, Integer Age, String emailAddress, String street,
                                             String number,String city,String postalCode,String country, String photoUrl,String photoPublicId , String phoneNumber){
        this.personName= new PersonName(firstName, lastName);
        this.dni=new Dni(dni);
        this.age= new Age(Age);
        this.birthDate=new BirthDate(birthDate);

        updateAddress(street, number, city, postalCode, country);
        updateEmail(emailAddress);
        updatePhoto(new Photo(photoUrl,photoPublicId));
        updatePhoneNumber(phoneNumber);
        return this;


    }
}
