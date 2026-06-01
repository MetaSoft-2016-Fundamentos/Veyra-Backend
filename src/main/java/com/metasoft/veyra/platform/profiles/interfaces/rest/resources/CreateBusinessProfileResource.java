package com.metasoft.veyra.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record CreateBusinessProfileResource(

        @Schema(example = "Clínica San Juan S.A.C.")
        @NotBlank(message = "Business name is required")
        @Size(min = 2, max = 200, message = "Business name must be between 2 and 200 characters")
        String businessName,

        @Schema(example = "contacto@clinicasanjuan.com")
        @NotBlank(message = "Email address is required")
        @Email(message = "Email address must be valid")
        @Size(max = 100, message = "Email address must not exceed 100 characters")
        String emailAddress,

        @Schema(example = "+51987654321")
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9\\s()-]{7,20}$", message = "Phone number format is invalid")
        String phoneNumber,

        @Schema(example = "Av. Javier Prado Este")
        @NotBlank(message = "Street is required")
        @Size(max = 150, message = "Street must not exceed 150 characters")
        String street,

        @Schema(example = "4200")
        @NotBlank(message = "Street number is required")
        @Size(max = 20, message = "Street number must not exceed 20 characters")
        String number,

        @Schema(example = "Lima")
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Schema(example = "15023")
        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @Schema(example = "Peru")
        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @NotBlank(message = "Photo is required")
        @Schema(description = "Business logo or profile photo", type = "string", format = "binary")
        MultipartFile photo,

        @Schema(example = "20601234567")
        @NotBlank(message = "RUC is required")
        @Pattern(regexp = "^[0-9]{11}$", message = "RUC must be exactly 11 digits")
        String ruc
) {}