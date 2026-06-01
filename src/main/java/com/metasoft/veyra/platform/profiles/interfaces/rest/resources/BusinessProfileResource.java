package com.metasoft.veyra.platform.profiles.interfaces.rest.resources;

public record BusinessProfileResource(Long id,String businessName,
                                      String emailAddress,
                                      String phoneNumber,
                                      String streetAddress,
                                      String photo,
                                      String ruc) {
}
