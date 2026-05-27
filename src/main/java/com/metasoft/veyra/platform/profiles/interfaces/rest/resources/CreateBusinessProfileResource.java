package com.metasoft.veyra.platform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateBusinessProfileResource(
        @NotBlank(message = "Business name is required")
        @Size(min = 2, max = 200, message = "Business name must be between 2 and 200 characters")
        String businessName,

        @NotBlank(message = "Email address is required")
        @Email(message = "Email address must be valid")
        @Size(max = 100, message = "Email address must not exceed 100 characters")
        String emailAddress,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9\\s()-]{7,20}$", message = "Phone number format is invalid")
        String phoneNumber,

        @NotBlank(message = "Street is required")
        @Size(max = 150, message = "Street must not exceed 150 characters")
        String street,

        @NotBlank(message = "Street number is required")
        @Size(max = 20, message = "Street number must not exceed 20 characters")
        String number,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @NotBlank(message = "Photo is required")
        @Size(max = 5000000, message = "Photo size must not exceed 5MB (base64)")
        String photoBase64,

        @NotBlank(message = "RUC is required")
        @Pattern(regexp = "^[0-9]{11}$", message = "RUC must be exactly 11 digits")
        String ruc
) {
}