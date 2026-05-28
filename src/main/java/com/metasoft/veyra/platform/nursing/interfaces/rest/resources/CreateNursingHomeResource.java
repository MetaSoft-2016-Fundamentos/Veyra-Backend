package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public record CreateNursingHomeResource(@NotBlank(message = "BusinessName is required"  )
                                        @Size(min = 3, max = 90, message = "Business name must be between 2 and 90 characters")

                                        @Schema(example = "Casa de reposo ")
                                        String businessName,

                                        @NotBlank(message = " Email is required" )
                                        @Schema(example = "example@hotmail.como")
                                        @Email(message = "Email address must be valid")
                                        @Size(max = 100, message = "Email must not exceed 100 characters" )
                                        String emailAddress,

                                         @NotBlank(message = " Phone number is required" )
                                        @Pattern(regexp = "^\\+?[0-9\\s()-]{7,20}$", message = "Phone number format is invalid")
                                        @Schema(example = " 912932912")
                                        String phoneNumber,

                                         @NotBlank(message = " Street is required" )
                                        @Schema(example = "Av Chorrillos")
                                        String street,

                                         @NotBlank(message = " Number is required" )
                                        @Schema(example = "12")
                                        String number,

                                        @NotBlank(message = " City is required" )
                                        @Size(max = 100, message = "City must not exceed 100 characters")
                                        @Schema(example = "Lima")
                                        String city,

                                         @NotBlank(message = " Postal code is required" )
                                        @Schema(example = "0123")
                                        String postalCode,

                                        @NotBlank(message = " Country is required" )
                                        @Schema(example = "Peru")
                                        String country,

                                         @NotNull(message = " Photo is required" )
                                        @Schema(description = "Profile photo", type = "string", format = "binary")
                                        MultipartFile photo,

                                        @NotBlank(message = "Ruc  is required" )
                                        @Pattern(regexp = "^[0-9]{11}$", message = "Ruc must be exactly 11 digits")
                                        String ruc) {}
