package com.metasoft.veyra.platform.profiles.domain.model.aggregates;

import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.BusinessName;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Photo;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Ruc;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class BusinessProfile extends Profile{
@Embedded
    private BusinessName businessName;
@Embedded
private Ruc ruc;
protected  BusinessProfile(){super();}
    public BusinessProfile(String businessName,
                           String emailAddress,
                           String phoneNumber,
                           String street,
                           String number,
                           String city,
                           String postalCode,
                           String country,
                           String photoUrl,
                           String photoPublicId,
                           String ruc){
    super(emailAddress,street,number,city,postalCode,country, new Photo( photoUrl,
             photoPublicId),phoneNumber);
     this.businessName= new BusinessName(businessName);
     this.ruc= new Ruc(ruc);

    }
}
