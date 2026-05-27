package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateNursingHomeResource(@NotBlank(message = "BusinessName is required"  ) String businessName,

                                        @NotBlank(message = " Email is required" ) String emailAddress,

                                        @NotBlank(message = " Phone number is required" ) String phoneNumber,

                                        @NotBlank(message = " Street is required" ) String street,

                                        @NotBlank(message = " Number is required" ) String number,

                                        @NotBlank(message = " City is required" ) String city,

                                        @NotBlank(message = " Postal code is required" ) String postalCode,

                                        @NotBlank(message = " Country is required" ) String country,

                                        @NotBlank(message = " Photo is required" ) String photoBase64,

                                        @NotBlank(message = "Ruc  is required" ) String ruc) {}
