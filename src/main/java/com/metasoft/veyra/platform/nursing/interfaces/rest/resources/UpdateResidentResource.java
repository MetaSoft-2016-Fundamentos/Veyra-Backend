package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateResidentResource(@NotBlank(message = "DNI is required") String dni,

                         @NotBlank(message = "First name is required") String firstName,

                         @NotBlank(message = "Last name is required") String lastName,

                         @NotNull(message = "Birth date is required")LocalDate birthDate,

                         @NotNull(message = "Age is required")Integer age,

                         @NotBlank(message = "Email address is required")String emailAddress,

                         @NotBlank(message = "Street is required")String street,

                         @NotBlank(message = "Number is required")String number,

                         @NotBlank(message = "City is required")String city,

                         @NotBlank(message = "Postal code is required")String postalCode,

                         @NotBlank(message = "Country is required") String country,

                         @NotBlank(message = "Photo Base64 is required")String photoBase64,

                         @NotBlank(message = "Phone number is required") String phoneNumber,

                         @NotBlank(message = "Legal representative first name is required")String legalRepresentativeFirstName,

                         @NotBlank(message = "Legal representative last name is required")String legalRepresentativeLastName,

                         @NotBlank(message = "Legal representative phone number is required")  String legalRepresentativePhoneNumber,

                         @NotBlank(message = "Emergency contact first name is required")String emergencyContactFirstName,

                         @NotBlank(message = "Emergency contact last name is required")String emergencyContactLastName,

                         @NotBlank(message = "Emergency contact phone number is required")String emergencyContactPhoneNumber) {}
