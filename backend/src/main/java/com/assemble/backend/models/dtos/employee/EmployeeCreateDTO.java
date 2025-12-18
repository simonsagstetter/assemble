/*
 * assemble
 * EmployeeCreateDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.employee;

import com.assemble.backend.models.entities.employee.Address;
import com.assemble.backend.models.entities.employee.BankAccount;
import com.assemble.backend.models.entities.employee.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
@Schema
public class EmployeeCreateDTO {

    @Nullable
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String userId;

    @NonNull
    @NotBlank
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Max"
    )
    private String firstname;

    @NonNull
    @NotBlank
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Mustermann"
    )
    private String lastname;


    @Nullable
    @Email
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "max.mustermannl@example.com"
    )
    private String email;

    @Nullable
    @Size(min = 3, max = 15)
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "+491234567890"
    )
    private String phone;

    @Nullable
    @Past
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "date",
            example = "2025-01-01"
    )
    private LocalDate dateOfBirth;

    @Nullable
    @Size(min = 1, max = 100)
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "Berlin"
    )
    private String placeOfBirth;

    @Nullable
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "MaritalStatus",
            example = "Single"
    )
    private MaritalStatus maritalStatus;

    @Nullable
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "Address"
    )
    private Address address;

    @Nullable
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "BankAccount"
    )
    private BankAccount bankAccount;

    @Nullable
    @Size(min = 1, max = 30)
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "german"
    )
    private String citizenship;

    @Nullable
    @Size(min = 1, max = 100)
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "TK Nord"
    )
    private String healthInsurance;

    @Nullable
    @Size(min = 1, max = 100)
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String nationalInsuranceNumber;

    @Nullable
    @Size(min = 1, max = 100)
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String taxIdentificationNumber;

}
