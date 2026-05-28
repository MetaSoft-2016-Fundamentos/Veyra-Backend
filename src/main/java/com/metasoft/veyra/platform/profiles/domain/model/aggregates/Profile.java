package com.metasoft.veyra.platform.profiles.domain.model.aggregates;

import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.PhoneNumber;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Photo;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.StreetAddress;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class Profile extends AuditableAbstractAggregateRoot<Profile> {
    @Embedded
    private EmailAddress emailAddress;
    @Embedded
    private  StreetAddress streetAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "photoUrl", column = @Column(name = "photo_url", nullable = false)),
            @AttributeOverride(name = "photoPublicId", column = @Column(name = "photo_public_id", nullable = false))
    })
    private Photo photo;
    @Embedded
    private PhoneNumber phoneNumber;
    protected Profile() {
    }
    protected Profile(String emailAddress,  String street,
                      String number,
                      String city,
                      String postalCode,
                      String country, Photo image, String phoneNumber) {
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.emailAddress = new EmailAddress(emailAddress);
        this.streetAddress = new StreetAddress(street, number, city, postalCode, country);
        this.photo = image;
    }
    public void updateEmail(String newEmail) {
        this.emailAddress = new EmailAddress(newEmail);
    }

    public void updateAddress(String street,
                              String number,
                              String city,
                              String postalCode,
                              String country) {
        this.streetAddress = new StreetAddress( street, number, city, postalCode, country);
    }
    public void updatePhoto(Photo photo) {
        this.photo =photo;
    }
    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = new PhoneNumber( newPhoneNumber);
    }

}
