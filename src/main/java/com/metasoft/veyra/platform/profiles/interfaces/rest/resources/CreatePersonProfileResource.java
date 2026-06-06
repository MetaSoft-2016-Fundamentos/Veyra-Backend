package com.metasoft.veyra.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record CreatePersonProfileResource(

        @Schema(example = "12345678")
        @NotBlank(message = "DNI is required")
        @Pattern(regexp = "^[0-9]{8}$", message = "DNI must be exactly 8 digits")
        String dni,

        @Schema(example = "Carlos")
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        String firstName,

        @Schema(example = "Pérez")
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        String lastName,

        @Schema(example = "1990-05-15")
        @NotNull(message = "Birth date is required")
        LocalDate birthDate,

        @Schema(example = "34")
        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age must be a positive number")
        @Max(value = 120, message = "Age must be a realistic value")
        Integer age,

        @Schema(example = "carlos@mail.com")
        @NotBlank(message = "Email address is required")
        @Email(message = "Email address must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String emailAddress,

        @Schema(example = "Av. Lima")
        @NotBlank(message = "Street is required")
        @Size(max = 150, message = "Street must not exceed 150 characters")
        String street,

        @Schema(example = "123")
        @NotBlank(message = "Street number is required")
        @Size(max = 20, message = "Street number must not exceed 20 characters")
        String number,

        @Schema(example = "Lima")
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Schema(example = "15001")
        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @Schema(example = "Peru")
        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @Schema(example = "+51987654321")
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9\\s()-]{7,20}$", message = "Phone number format is invalid")
        String phoneNumber,

        @NotBlank(message = "Photo is required")
        @Schema(description = "Profile photo", type = "string", format = "binary")
        MultipartFile photo
) {}