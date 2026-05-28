package com.metasoft.veyra.platform.shared.domain.model.valueobjects;

import jakarta.validation.constraints.Email;

public record EmailAddress( @Email String emailAddress ) {
    public  EmailAddress{
       if (emailAddress==null|| emailAddress.isBlank()){
              throw new IllegalArgumentException("Email address cannot be null or blank");
       }
    }
}
