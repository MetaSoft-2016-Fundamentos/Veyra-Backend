package com.metasoft.veyra.platform.profiles.domain.exceptions;

public class BusinessNameAlreadyExists extends RuntimeException {
    public BusinessNameAlreadyExists(String businessName) {
        super(String.format("Business name '%s' already exists.", businessName));
    }
}
